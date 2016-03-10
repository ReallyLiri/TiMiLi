package com.android.callmemaybe.gistService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Liri on 05/02/2016.
 */
public class GistWakeupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GistWakeupReceiver", "Got intent with code " + intent.getAction());
        GistService.sendWakeup(context);
    }
}
