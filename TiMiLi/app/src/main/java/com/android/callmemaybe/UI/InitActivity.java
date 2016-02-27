package com.android.callmemaybe.UI;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.callmemaybe.UI.databinding.ActivityInitBinding;
import com.android.callmemaybe.helpers.PhoneNumberHelper;
import com.android.callmemaybe.helpers.SharedPreferencesHelper;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class InitActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "CCZ861CPopx0qF1wdSi0YEkdh";
    private static final String TWITTER_SECRET = "ZEjchNJs1w2aRCAyi5ssZYow9qCuLME2f3BAtUmKFXPaDwFPZF ";

    PhoneNumberHelper mPhoneNumberHelper;
    DigitsAuthButton mAuthButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInitBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_init);
        mAuthButton = binding.getPhoneNumberButton;

        mPhoneNumberHelper = new PhoneNumberHelper();

        String myNumber = mPhoneNumberHelper.getMyPhoneNumber(this);
        if (myNumber == null || myNumber.length() == 0) {
            mAuthButton.setVisibility(View.VISIBLE);
            getMyNumber();
        }
        else {
            MainActivity.startMainActivity(this);
        }
    }

    private void getMyNumber() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        mAuthButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Toast.makeText(getApplicationContext(), "Phone number authentication succeeded with: " + phoneNumber, Toast.LENGTH_LONG).show();
                mPhoneNumberHelper.setMyPhoneNumber(InitActivity.this, phoneNumber);
                MainActivity.startMainActivity(InitActivity.this);
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
                Toast.makeText(getApplicationContext(), "Phone number authentication failed with exception", Toast.LENGTH_LONG).show();
            }
        });

    }
}
