package com.android.callmemaybe.gistService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.Calendar;

/**
 * Created by Liri on 05/02/2016.
 */
public class GistService extends Service implements SensorEventListener {

    private static final String EXTRA_OPCODE = "EXTRA_OPCODE";

    private static final int OPCODE_NOP = 0;
    private static final int OPCODE_WAKEUP = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int opcode = intent.getIntExtra(EXTRA_OPCODE, OPCODE_NOP);

        switch (opcode)
        {
            case OPCODE_WAKEUP:
                RegisterForSensorUpdates();
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
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + millisec);

        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    private void calculateAndSendGist(boolean isDeviceStill) {
        ICloudServer cloudServer = new FireBaseCloudServer(this);
        GistCalculator calculator = new GistCalculator();
        UserGist gist = calculator.GetGist(this, isDeviceStill);
        cloudServer.UpdateMyGist(gist);
        goToSleep(gist.serviceSleepTimeInMillisec());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // -------------------- Sensor handling:

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float[] prevValues = null;

    public void RegisterForSensorUpdates() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            if (prevValues == null) {
                prevValues = event.values.clone();
                return;
            }

            float diff = vectorDiff(event.values);

            prevValues = event.values.clone();

            mSensorManager.unregisterListener(this, mAccelerometer);

            Log.d("PhysicalMovementHelper", "Diff is " + diff);
            boolean isDeviceStill = diff < 0.02;
            calculateAndSendGist(isDeviceStill);
        }
    }

    private float vectorDiff(float[] values) {
        float dx = Math.abs(values[0] - prevValues[0]);
        float dy = Math.abs(values[1] - prevValues[1]);
        float dz = Math.abs(values[2] - prevValues[2]);
        return dx + dy + dz;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // NOP
    }
}
