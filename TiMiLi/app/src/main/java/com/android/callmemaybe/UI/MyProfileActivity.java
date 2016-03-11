package com.android.callmemaybe.UI;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.databinding.ActivityMyProfileBinding;
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    private EditText funnyStatus;
    private Button saveButton;
    private Contact myContact;
    private Button goToUnblockedActivity;
    private Toolbar toolbar;

    private Button[] blocked_days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyProfile", "in onCreate");

        myContact = ContactHelper.getMyContact(this);

        ActivityMyProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        binding.setContact(myContact);

        toolbar = binding.myProfileToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //allows up navigation

        this.funnyStatus = binding.myFunnyStatus;
        Log.d("MyProfile", "phoneNum = " + myContact);

        this.saveButton = binding.saveAllChanges;

        this.blocked_days = new Button[7];
        this.blocked_days[0] = binding.sun;
        this.blocked_days[1] = binding.mon;
        this.blocked_days[2] = binding.tue;
        this.blocked_days[3] = binding.wed;
        this.blocked_days[4] = binding.thu;
        this.blocked_days[5] = binding.fri;
        this.blocked_days[6] = binding.sat;

        final String[] debugArray = new String[]{"sun", "mon", "tue", "wed", "thu", "fri", "sat"};

        final boolean[] updatedActiveDays = new boolean[7];
        for (int i = 0; i < 7; i++){
            updatedActiveDays[i] = myContact.contactStatus.inactiveDays.contains(i+1);
        }

        for (int i = 0; i < 7; i++){
            final Integer j = i;
            blocked_days[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updatedActiveDays[j]){
                        updatedActiveDays[j] = false;
                        myContact.contactStatus.inactiveDays.remove((Object) (j+1));
                        blocked_days[j].setText(debugArray[j] + "blocked");
                    }
                    else{
                        updatedActiveDays[j] = true;
                        myContact.contactStatus.inactiveDays.add(j+1);
                        blocked_days[j].setText(debugArray[j] + "free");
                    }
                }
            });
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String funnyStatusString = funnyStatus.getText().toString();
                if (!funnyStatusString.equals(myContact.getFunnyStatus())){
                    myContact.setFunnyStatus(funnyStatusString);
                }
                List<Integer> old = myContact.contactStatus.inactiveDays;
                List<Integer> newList = getUpdatedInactiveDays(updatedActiveDays);
                if (!old.equals(newList)){
                    myContact.contactStatus.inactiveDays = newList;
                }
                ContactHelper.updateMyContact(MyProfileActivity.this);
                saveToServer();
            }
        });

        this.goToUnblockedActivity = binding.goToUnblocked;
        goToUnblockedActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, UnblockUsers.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_toolbar, menu);
        toolbar.setTitle(R.string.my_profile);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                NavUtils.navigateUpTo(this, upIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Integer> getUpdatedInactiveDays(boolean[] activeDays){
        List<Integer> updated = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            if (activeDays[i]){
                updated.remove((Object) (i + 1));
            }
            else{
                updated.add(i+1);
            }
        }
        return updated;
    }

    private void saveToServer() {
        ICloudServer server = new FireBaseCloudServer(this);
        server.UpdateMyStatus(myContact.getContactStatus());
    }

}
