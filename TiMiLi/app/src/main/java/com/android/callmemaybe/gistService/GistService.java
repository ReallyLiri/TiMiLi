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
        goToSleep(5 * 1000); // TODO
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // -------------------- Sensor handling:

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private double mLastAccel = SensorManager.GRAVITY_EARTH;
    private double mCurrentAccel = SensorManager.GRAVITY_EARTH;
    private double mAggregatedAccel = 0.00f;
    private int mSensorHitCount = 0;

    public void RegisterForSensorUpdates() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mSensorHitCount++;

            float[] gravity = event.values.clone();
            float x = gravity[0];
            float y = gravity[1];
            float z = gravity[2];
            mLastAccel = mCurrentAccel;
            mCurrentAccel = Math.sqrt(x * x + y * y + z * z);
            double delta = mCurrentAccel - mLastAccel;
            mAggregatedAccel = mAggregatedAccel * 0.9f + delta;

            if (mSensorHitCount < 2) {
                return;
            }

            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorHitCount = 0;
            Log.d("PhysicalMovementHelper", "Aggregate Accelaration is now " + mAggregatedAccel);
            boolean isDeviceStill = mAggregatedAccel <= 3;
            calculateAndSendGist(isDeviceStill);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // NOP
    }
}
