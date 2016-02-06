package com.android.callmemaybe.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

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

    public Set<String> GetStringSet(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        return prefs.getStringSet(key, null);
    }

    public void PutStringSet(Context context, String key, Set<String> value) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(key, value).apply();
    }

    public<T extends Enum<T>> T GetEnum(Context context, String key, Class enumClass, T defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        String name = prefs.getString(key, null);
        if (name == null) {
            return defaultValue;
        }
        return (T) Enum.valueOf(enumClass, name);
    }

    public<T extends Enum<T>> void PutEnum(Context context, String key, T value) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        prefs.edit().putString(key, value.name()).apply();
    }
}
