package com.android.callmemaybe.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
    }

    /*
    for each contact on device, ask server if contact exists, register for updates & save contacts dict on shared pref
     */
    protected void initContact(){

    }
}
