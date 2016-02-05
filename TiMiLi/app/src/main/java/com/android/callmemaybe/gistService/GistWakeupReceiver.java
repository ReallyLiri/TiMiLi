package com.android.callmemaybe.gistService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Liri on 05/02/2016.
 */
public class GistWakeupReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GistService.sendWakeup(context);
    }
}
