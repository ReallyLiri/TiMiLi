package com.android.callmemaybe.contracts;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Liri on 05/02/2016.
 */
public class UserGist {
    public String userId;
    public long timestamp;
    public int dayOfWeek; // [1,7]
    public int hourOfDay; // [0,23]
    public int minuteOfHour; // [0,59]
    public boolean isDeviceStill;
    public RingerMode ringerMode;
    public boolean isScreenOn;

    public ActiveInPractice IsActiveInPractice(String myId, UserStatus userStatus) {
        if (userStatus.userProfile == UserProfile.DoNotDisturb) {
            return ActiveInPractice.Unwilling;
        }

        if (userStatus.blockedUsers.contains(myId)) {
            return ActiveInPractice.Unwilling;
        }

        Calendar gistDate = Calendar.getInstance();
        gistDate.setTimeInMillis(timestamp);
        if (userStatus.inactiveDays.contains(gistDate.get(Calendar.DAY_OF_WEEK))) {
            return ActiveInPractice.Unwilling;
        }

        long timeSince = System.currentTimeMillis() - timestamp;
        long minutesSince = TimeUnit.MILLISECONDS.toMinutes(timeSince);
        if (minutesSince > 10) {
            return ActiveInPractice.Unknown;
        }

        if (isScreenOn) {
            return ActiveInPractice.Active;
        }

        if (ringerMode == RingerMode.Silent) {
            return ActiveInPractice.Inactive;
        }

        if (isDeviceStill) {
            return ActiveInPractice.Inactive;
        }

        return ActiveInPractice.Active;
    }

    public long serviceSleepTimeInMillisec() {
        return 10 * 1000; // 10 sec  // TODO
    }
}
