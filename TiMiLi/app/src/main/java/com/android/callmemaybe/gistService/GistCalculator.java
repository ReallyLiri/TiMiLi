package com.android.callmemaybe.gistService;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;

import com.android.callmemaybe.contracts.RingerMode;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.helpers.PhoneNumberHelper;

import java.util.Calendar;

/**
 * Created by Liri on 05/02/2016.
 */
public class GistCalculator {

    public UserGist GetGist(Context context, boolean isDeviceStill, String myId) {
        UserGist gist = new UserGist();

        gist.userId = myId;

        gist.isScreenOn = IsScreenOn(context);

        SetTimes(gist);

        gist.ringerMode = GetRingerMode(context);

        gist.isDeviceStill = isDeviceStill;

        return gist;
    }

    private void SetTimes(UserGist gist) {
        Calendar calendar = Calendar.getInstance();
        gist.dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        gist.hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        gist.minuteOfHour = calendar.get(Calendar.MINUTE);
        gist.timestamp = calendar.getTimeInMillis();
    }

    private boolean IsScreenOn(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT < 20) {
            return powerManager.isScreenOn();
        }
        else {
            return powerManager.isInteractive();
        }
    }

    private RingerMode GetRingerMode(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                return RingerMode.Normal;
            case AudioManager.RINGER_MODE_SILENT:
                return RingerMode.Silent;
            case AudioManager.RINGER_MODE_VIBRATE:
                return RingerMode.Vibrate;
        }
        return null;
    }
}
