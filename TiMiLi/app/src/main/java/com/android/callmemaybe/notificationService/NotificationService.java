package com.android.callmemaybe.notificationService;

import android.app.Notification;
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
import com.android.callmemaybe.contracts.IOnLatestStatusUpdatedListener;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.contracts.UserStatus;
import com.android.callmemaybe.helpers.PhoneNumberHelper;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Liri on 06/02/2016.
 */
public class NotificationService extends Service implements IOnLatestGistUpdatedListener, IOnLatestStatusUpdatedListener {

    private static String LogTag = "NotificationService";

    private static String EXTRA_OPCODE = "EXTRA_OPCODE";
    private static String EXTRA_TRACKED_LIST = "EXTRA_TRACKED_LIST";

    private Set<String> mTrackedUsers = new HashSet<>();
    private Map<String, UserStatus> mStatuses = new HashMap<>();
    private Map<String, ActiveInPractice> mUserLastActive = new HashMap<>();
    private String mMyId;

    private final static int OPCODE_NOP = 0;
    private final static int OPCODE_TRACKED_LIST_CHANGED = 1;

    private final static int FOREGROUND_ID = 541;

    private ICloudServer mCloudServer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int opcode = intent.getIntExtra(EXTRA_OPCODE, OPCODE_NOP);

        if (mMyId == null) {
            PhoneNumberHelper phoneNumberHelper = new PhoneNumberHelper();
            mMyId = phoneNumberHelper.getMyPhoneNumber(this);
        }

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
        if (mCloudServer == null) {
            mCloudServer = new FireBaseCloudServer(this);
        }

        for (String userId : trackedSet) {
            if (!mTrackedUsers.contains(userId)) {
                // new tracked user
                mCloudServer.RegisterForUserStatusData(userId, this);
                // We will register for gist only after the first status will arrive
            }
        }
        for (String userId : mTrackedUsers) {
            if (!trackedSet.contains(userId)) {
                // tracked user removed
                mCloudServer.UnRegisterForUserGistData(userId);
                mCloudServer.UnRegisterForUserStatusData(userId);
                mStatuses.remove(userId);
                mUserLastActive.remove(userId);
            }
        }

        mTrackedUsers = trackedSet;

        if (mTrackedUsers.isEmpty()) {
            stopForeground(true);
        }
        else {
            startForeground();
        }
    }

    private void startForeground() {
        int notificationId = this.hashCode();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Notification service")
                        .setContentText("... is running").build();
        startForeground(FOREGROUND_ID, notification);
    }

    @Override
    public void latestGistUpdated(UserGist latestGist) {
        String userId = latestGist.userId;
        UserStatus status = mStatuses.get(userId);

        if (status == null) {
            Log.e(LogTag, "Got gist but no status yet, skipping for user " + userId);
        }

        ActiveInPractice activeInPractice = latestGist.IsActiveInPractice(mMyId, status);

        applyUserActiveInPracticeInfo(userId, activeInPractice, status);

        mUserLastActive.put(userId, activeInPractice);
    }

    private void applyUserActiveInPracticeInfo(String userId, ActiveInPractice activeInPractice, UserStatus userStatus) {
        int notificationId = userId.hashCode();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (activeInPractice != ActiveInPractice.Active) {
            // user is not active. update stored value
            notificationManager.cancel(notificationId);
            return;
        }

        ActiveInPractice prevActivity = mUserLastActive.get(userId);
        if (prevActivity != null && prevActivity == ActiveInPractice.Active) {
            // user was already active, no need for further notification
            return;
        }

        Log.d(LogTag, "User was found as active, sending notification! Id = " + userId);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("User is active!")
                        .setContentText("Id: " + userId);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    @Override
    public void latestStatusUpdated(UserStatus latestStatus) {
        String userId = latestStatus.phoneNum;
        boolean existed = mStatuses.containsKey(userId);
        mStatuses.put(userId, latestStatus);

        if (mCloudServer.IsRegisteredForUserGistData(userId)) {
            mCloudServer.RegisterForUserGistData(userId, this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
