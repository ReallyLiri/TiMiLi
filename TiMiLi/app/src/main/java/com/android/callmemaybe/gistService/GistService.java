package com.android.callmemaybe.gistService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.Calendar;

/**
 * Created by Liri on 05/02/2016.
 */
public class GistService extends Service {

    private static final String EXTRA_OPCODE = "EXTRA_OPCODE";

    private static final int OPCODE_NOP = 0;
    private static final int OPCODE_WAKEUP = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int opcode = intent.getIntExtra(EXTRA_OPCODE, OPCODE_NOP);

        switch (opcode)
        {
            case OPCODE_WAKEUP:
                calculateAndSendGist();
                goToSleep(5 * 1000); // TODO
                break;

            case OPCODE_NOP:
                return super.onStartCommand(intent, flags, startId);
        }

        return Service.START_STICKY;
    }

    public static void sendWakeup(Context context) {
        Intent serviceIntent = new Intent(context, GistService.class);
        serviceIntent.putExtra(EXTRA_OPCODE, OPCODE_WAKEUP);
        context.startService(serviceIntent);
    }

    private void goToSleep(long millisec) {
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent receiverIntent = new Intent(this, GistWakeupReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, receiverIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + millisec);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    private void calculateAndSendGist() {
        ICloudServer cloudServer = new FireBaseCloudServer(this);
        GistCalculator calculator = new GistCalculator();
        UserGist gist = calculator.GetGist(this);
        cloudServer.UpdateMyGist(gist);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
