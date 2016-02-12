package com.android.callmemaybe.server;

import android.content.Context;

import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.IOnLatestGistUpdatedListener;
import com.android.callmemaybe.contracts.IOnLatestStatusUpdatedListener;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.contracts.UserStatus;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liri on 05/02/2016.
 */
public class FireBaseCloudServer implements ICloudServer {
    private Map<String, ChildEventListener> mGistListeners;
    private Map<String, ValueEventListener> mStatusListeners;

    private final String BaseFirebaseURL = "https://timili.firebaseio.com/android";

    public FireBaseCloudServer(Context context) {
        Firebase.setAndroidContext(context);
        mGistListeners = new HashMap<>();
        mStatusListeners = new HashMap<>();
    }

    private String GetUserGistDirectory(String userId) {
        return BaseFirebaseURL + "/users/user_" + userId;
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
                UserGist latestGist = snapshot.getValue(UserGist.class);
                onLatestGistUpdated.latestGistUpdated(latestGist);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                throw new IllegalStateException("onChildChanged should not be triggered");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                throw new IllegalStateException("onChildRemoved should not be triggered");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                throw new IllegalStateException("onChildMoved should not be triggered");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw new IllegalStateException("onCancelled should not be triggered");
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

    private String GetUserStatusFile(String userId) {
        return BaseFirebaseURL + "/users/all/" + userId;
    }

    @Override
    public void UpdateMyStatus(UserStatus myStatus) {
        Firebase ref = new Firebase(GetUserStatusFile(myStatus.userId));
        ref.setValue(myStatus);
    }

    @Override
    public void RegisterForUserStatusData(String userId, final IOnLatestStatusUpdatedListener onLatestStatusUpdated) {
        Firebase ref = new Firebase(GetUserStatusFile(userId));
        ValueEventListener listener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserStatus latestStatus = dataSnapshot.getValue(UserStatus.class);
                onLatestStatusUpdated.latestStatusUpdated(latestStatus);
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
}
