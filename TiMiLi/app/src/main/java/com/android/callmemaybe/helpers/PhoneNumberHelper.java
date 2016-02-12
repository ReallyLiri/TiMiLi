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
        if (phoneNumber != null) {
            return phoneNumber;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumber = telephonyManager.getLine1Number();

        if (phoneNumber != null) {
            mSharedPreferencesHelper.PutString(context, KEY_PHONE_NUMBER, phoneNumber);
        }

        return phoneNumber;
    }

    private void setMyPhoneNumber(Context context, String phoneNumber) {
        mSharedPreferencesHelper.PutString(context, KEY_PHONE_NUMBER, phoneNumber);
    }

    public void brutallyGetMyPhoneNumber(final Context context) {

        final ContentResolver contentResolver = context.getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms/sent"), true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);

                Uri uriSMSURI = Uri.parse("content://sms/sent");
                Cursor cur = contentResolver.query(uriSMSURI, null, null, null, null);
                cur.moveToNext();
                String smsNumber = cur.getString(cur.getColumnIndex("address"));
                Log.d("SMSListener", "Got SMS with add " + smsNumber);
                PhoneNumberHelper.this.setMyPhoneNumber(context, smsNumber);
                contentResolver.unregisterContentObserver(this);
                GistService.sendStartup(context);
            }
        });

        Log.d("SMSListener", "Sending");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("0547703703", null, "Hello World", null, null);
    }
}
