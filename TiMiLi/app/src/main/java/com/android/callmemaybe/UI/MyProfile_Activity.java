package com.android.callmemaybe.UI;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.databinding.ActivityMyProfileBinding;
import com.android.callmemaybe.helpers.ContactHelper;

public class MyProfile_Activity extends AppCompatActivity {

    EditText funnyStatus;
    Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_);
        Log.d("MyProfile", "in onCreate");

        final Contact myContact = ContactHelper.getMyContact(this);

        ActivityMyProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile_);
        binding.setContact(myContact);

        this.funnyStatus = (EditText) findViewById(R.id.my_funny_status);
        Log.d("MyProfile", "phoneNum = " + myContact);
        funnyStatus.setText(myContact.getFunnyStatus());

        this.saveButton = (Button) findViewById(R.id.save_all_changes);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String funnyStatusString = funnyStatus.getText().toString();
                if (!funnyStatusString.equals(myContact.getFunnyStatus())){
                    myContact.setFunnyStatus(funnyStatusString);
                }
            }
        });
    }
}
