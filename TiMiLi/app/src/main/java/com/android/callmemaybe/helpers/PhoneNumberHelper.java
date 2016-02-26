package com.android.callmemaybe.helpers;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.callmemaybe.gistService.GistService;

/**
 * Created by Liri on 05/02/2016.
 */
public class PhoneNumberHelper {
    private SharedPreferencesHelper mSharedPreferencesHelper;

    private static final String KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER";

    public PhoneNumberHelper() {
        mSharedPreferencesHelper = new SharedPreferencesHelper();
    }

    public String getMyPhoneNumber(Context context){
        String phoneNumber = mSharedPreferencesHelper.GetString(context, KEY_PHONE_NUMBER);
        return phoneNumber;
    }

    public void setMyPhoneNumber(Context context, String phoneNumber) {
        mSharedPreferencesHelper.PutString(context, KEY_PHONE_NUMBER, phoneNumber);
    }
}
