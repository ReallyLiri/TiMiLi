package com.android.callmemaybe.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Liri on 05/02/2016.
 */
public class SharedPreferencesHelper {

    private static final String IDENTITY = "com.android.callmemaybe.timili";

    public String GetString(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public void PutString(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        prefs.edit().putString(key, value).apply();
    }
}
