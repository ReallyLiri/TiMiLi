package com.android.callmemaybe.UI;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.callmemaybe.UI.databinding.ContactFragmentBinding;
import com.android.callmemaybe.helpers.ContactHelper;

public class MyProfile_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_);
        Log.d("MyProfile", "in onCreate");

        ContactFragmentBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile_);
        binding.setContact(ContactHelper.getMyContact(this));
    }
}
