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
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.server.FireBaseCloudServer;

public class MyProfile_Activity extends AppCompatActivity {

    private EditText funnyStatus;
    private Button saveButton;
    private Contact myContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyProfile", "in onCreate");

        myContact = ContactHelper.getMyContact(this);

        ActivityMyProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile_);
        binding.setContact(myContact);

        this.funnyStatus = binding.myFunnyStatus;
        Log.d("MyProfile", "phoneNum = " + myContact);

        this.saveButton = binding.saveAllChanges;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String funnyStatusString = funnyStatus.getText().toString();
                if (!funnyStatusString.equals(myContact.getFunnyStatus())){
                    myContact.setFunnyStatus(funnyStatusString);
                }
                saveToServer();
            }
        });
    }

    private void saveToServer() {
        ICloudServer server = new FireBaseCloudServer(this);
        server.UpdateMyStatus(myContact.getContactStatus());
    }
}
