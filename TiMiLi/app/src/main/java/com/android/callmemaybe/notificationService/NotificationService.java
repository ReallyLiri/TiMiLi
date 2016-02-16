package com.android.callmemaybe.notificationService;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.callmemaybe.contracts.ActiveInPractice;
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.IOnLatestGistUpdatedListener;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.helpers.SharedPreferencesHelper;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Liri on 06/02/2016.
 */
public class NotificationService extends Service implements IOnLatestGistUpdatedListener {

    private static String EXTRA_OPCODE = "EXTRA_OPCODE";
    private static String EXTRA_TRACKED_LIST = "EXTRA_TRACKED_LIST";

    private static String KEY_TRACKED_SET = "KEY_TRACKED_SET";
    private static String KEY_USER_ACTIVITY = "KEY_USER_ACTIVITY";

    private final static int OPCODE_NOP = 0;
    private final static int OPCODE_TRACKED_LIST_CHANGED = 1;

    private ICloudServer mCloudServer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int opcode = intent.getIntExtra(EXTRA_OPCODE, OPCODE_NOP);

        switch (opcode) {
            case OPCODE_TRACKED_LIST_CHANGED:
                List<String> trackedList = intent.getStringArrayListExtra(EXTRA_TRACKED_LIST);
                Set<String> trackedSet = new HashSet<>(trackedList);
                setupTrackedListeners(trackedSet);
                return Service.START_STICKY;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static void NotifyOfTrackedListChanged(Context context, ArrayList<String> tracked) {
        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra(EXTRA_OPCODE, OPCODE_TRACKED_LIST_CHANGED);
        serviceIntent.putStringArrayListExtra(EXTRA_TRACKED_LIST, tracked);
    }

    private void setupTrackedListeners(Set<String> trackedSet) {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper();
        Set<String> prevSet = sharedPreferencesHelper.GetStringSet(this, KEY_TRACKED_SET);
        if (prevSet == null) {
            prevSet = new HashSet<>();
        }

        if (mCloudServer == null) {
            mCloudServer = new FireBaseCloudServer(this);
        }

        for (String userId : trackedSet) {
            if (!prevSet.contains(userId)) {
                // new tracked user
                mCloudServer.RegisterForUserGistData(userId, this);
            }
        }
        for (String userId : prevSet) {
            if (!trackedSet.contains(userId)) {
                // tracked user removed
                mCloudServer.UnRegisterForUserGistData(userId);
            }
        }

        sharedPreferencesHelper.PutStringSet(this, KEY_TRACKED_SET, trackedSet);
    }

    @Override
    public void latestGistUpdated(UserGist latestGist) {
        ActiveInPractice activeInPractice = latestGist.IsActiveInPractice();
        String userId = latestGist.userId;
        String userKey = KEY_USER_ACTIVITY + "_" + userId;

        int notificationId = latestGist.userId.hashCode();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper();
        ActiveInPractice prevActivity =
                sharedPreferencesHelper.GetEnum(this, userKey, ActiveInPractice.class, ActiveInPractice.Unknown);

        if (activeInPractice != ActiveInPractice.Active) {
            // user is not active. update stored value
            sharedPreferencesHelper.PutEnum(this, userKey, activeInPractice);
            notificationManager.cancel(notificationId);
            return;
        }
        if (prevActivity == ActiveInPractice.Active) {
            // user was already active, no need for further notification
            return;
        }

        Log.d("NotificationService", "User was found as active, sending notification! Id = " + userId);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("User is active!")
                        .setContentText("Id: " + userId);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
