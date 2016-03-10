package com.android.callmemaybe.helpers;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

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

    public static String normalizePhoneNumber ( String phoneNumber) {
        return PhoneNumberUtil.normalizeDigitsOnly(phoneNumber);
    }
}
