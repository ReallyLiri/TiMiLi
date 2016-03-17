package com.android.callmemaybe.server;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.IOnLatestGistUpdatedListener;
import com.android.callmemaybe.contracts.IOnLatestStatusUpdatedListener;
import com.android.callmemaybe.contracts.IOnUserExistsResponse;
import com.android.callmemaybe.contracts.IOnUsersExistResponse;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.contracts.UserStatus;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Liri on 05/02/2016.
 */
public class FireBaseCloudServer implements ICloudServer {
    private ConcurrentHashMap<String, ChildEventListener> mGistListeners;
    private ConcurrentHashMap<String, ValueEventListener> mStatusListeners;

    private final String BaseFirebaseURL = "https://alleyes.firebaseio.com/android";

    public FireBaseCloudServer(Context context) {
        Firebase.setAndroidContext(context);
        mGistListeners = new ConcurrentHashMap<>();
        mStatusListeners = new ConcurrentHashMap<>();
    }

    private String GetUserGistDirectory(String userId) {
        return BaseFirebaseURL + "/users/user_" + userId;
    }

    @Override
    public void DoesUserExist(final String userId, final IOnUserExistsResponse onUserExistsResponse) {
        Firebase ref = new Firebase(GetUserStatusDir());
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onUserExistsResponse.OnResponse(userId, dataSnapshot.hasChild(userId));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw new IllegalStateException("onCancelled should not be triggered");
            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void DoesUsersExist(final Set<String> users, final IOnUsersExistResponse onUsersExistResponse) {
        Firebase ref = new Firebase(GetUserStatusDir());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> existsByUserId = new HashMap<>();
                for (String userId : users) {
                    existsByUserId.put(userId, dataSnapshot.hasChild(userId));
                }
                onUsersExistResponse.OnResponse(existsByUserId);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw new IllegalStateException("onCancelled should not be triggered");
            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void UpdateMyGist(UserGist myGist) {
        Firebase ref = new Firebase(GetUserGistDirectory(myGist.userId) + "/date_" + myGist.timestamp);
        ref.setValue(myGist);
    }

    @Override
    public boolean IsRegisteredForUserGistData(String userId) {
        return mGistListeners.containsKey(userId);
    }

    @Override
    public void RegisterForUserGistData(String userId, final IOnLatestGistUpdatedListener onLatestGistUpdated) {
        Firebase ref = new Firebase(GetUserGistDirectory(userId));
        Query queryRef = ref.orderByKey().limitToLast(1);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                try {
                    UserGist latestGist = snapshot.getValue(UserGist.class);
                    onLatestGistUpdated.latestGistUpdated(latestGist);
                }
                catch(Exception ex) {
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                throw new IllegalStateException("onChildChanged should not be triggered");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        };
        mGistListeners.put(userId, listener);
        queryRef.addChildEventListener(listener);
    }

    @Override
    public void UnRegisterForUserGistData(String userId) {
        ChildEventListener listener = mGistListeners.remove(userId);
        if (listener != null) {
            Firebase ref = new Firebase(GetUserGistDirectory(userId));
            Query queryRef = ref.orderByKey().limitToLast(1);
            queryRef.removeEventListener(listener);
        }
    }

    private String GetUserStatusDir() {
        return BaseFirebaseURL + "/users/all";
    }

    private String GetUserStatusFile(String userId) {
        return GetUserStatusDir() + "/" + userId;
    }

    @Override
    public void UpdateMyStatus(UserStatus myStatus) {
        Firebase ref = new Firebase(GetUserStatusFile(myStatus.phoneNum));
        ref.setValue(myStatus);
    }

    @Override
    public void RegisterForUserStatusData(String userId, final IOnLatestStatusUpdatedListener onLatestStatusUpdated) {
        Firebase ref = new Firebase(GetUserStatusFile(userId));
        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    UserStatus latestStatus = dataSnapshot.getValue(UserStatus.class);
                    onLatestStatusUpdated.latestStatusUpdated(latestStatus);
                }
                catch (Exception ex) {
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw new IllegalStateException("onCancelled should not be triggered");
            }
        };
        mStatusListeners.put(userId, listener);
        ref.addValueEventListener(listener);
    }

    @Override
    public void UnRegisterForUserStatusData(String userId) {
        ValueEventListener listener = mStatusListeners.remove(userId);
        if (listener != null) {
            Firebase ref = new Firebase(GetUserStatusFile(userId));
            ref.removeEventListener(listener);
        }
    }

    @Override
    public void UnRegisterAll() {
        for (String userId : mGistListeners.keySet()) {
            UnRegisterForUserGistData(userId);
        }
        for (String userId : mStatusListeners.keySet()) {
            UnRegisterForUserStatusData(userId);
        }
        mGistListeners.clear();
        mStatusListeners.clear();
    }
}
