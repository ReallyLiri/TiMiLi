package com.android.callmemaybe.helpers;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Liri on 05/02/2016.
 */
public class TelephonyHelper {
    private SharedPreferencesHelper mSharedPreferencesHelper;

    private static final String KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER";

    public TelephonyHelper() {
        mSharedPreferencesHelper = new SharedPreferencesHelper();
    }

    public String getMyPhoneNumber(Context context){
        String phoneNumber = mSharedPreferencesHelper.GetString(context, KEY_PHONE_NUMBER);
        if (phoneNumber != null) {
            return phoneNumber;
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumber = telephonyManager.getLine1Number();

        mSharedPreferencesHelper.PutString(context, KEY_PHONE_NUMBER, phoneNumber);
        return phoneNumber;
    }

    //gets a string of a phone number
    //returns a string containing only numbers
    public static String normalizePhoneNumber ( String phoneNumber) {
        StringBuilder norNum = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); i++) {
            Character c = phoneNumber.charAt(i);
            if ( (c >= '0') && (c <= '9')) {
                norNum.append(c);
            }
        }
        return norNum.toString();
    }
}
