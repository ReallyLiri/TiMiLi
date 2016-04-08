package com.android.callmemaybe.UI;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.databinding.ActivityMyProfileBinding;
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {

    private EditText funnyStatus;
    private Contact myContact;
    private Button goToUnblockedActivity;
    private Button[] blocked_days;
    private boolean isDirty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyProfile", "in onCreate");

        myContact = ContactHelper.getMyContact(this);
        isDirty = false;

        ActivityMyProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        binding.setContact(myContact);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //allows up navigation
        getSupportActionBar().setTitle(R.string.my_profile_title);

        this.funnyStatus = binding.myFunnyStatus;

        this.blocked_days = new Button[7];
        this.blocked_days[0] = binding.sun;
        this.blocked_days[1] = binding.mon;
        this.blocked_days[2] = binding.tue;
        this.blocked_days[3] = binding.wed;
        this.blocked_days[4] = binding.thu;
        this.blocked_days[5] = binding.fri;
        this.blocked_days[6] = binding.sat;

        TransformationMethod boldDayTransformationMethod = new TransformationMethod() {

            @Override
            public CharSequence getTransformation(CharSequence source, View view) {
                CharSequence newSequence = new SpannableString("");
                String[] words = source.toString().split(" ");
                for (String word : words) {
                    if (word.contains("day")) {
                        SpannableString boldOption = new SpannableString(word);
                        boldOption.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, word.length(),0);
                        newSequence = TextUtils.concat(newSequence, " ", boldOption);
                    }
                    else {
                        newSequence = TextUtils.concat(newSequence, " ", word);
                    }
                }
                return newSequence;
            }

            @Override
            public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

            }
        };

        for (int i = 0; i < 7; i++){
            final int dayInt = i + 1;
            blocked_days[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myContact.toggleBlockedDay(dayInt);
                    isDirty = true;
                }
            });
            blocked_days[i].setTransformationMethod(boldDayTransformationMethod);
        }

        this.goToUnblockedActivity = binding.goToUnblocked;
        if (!myContact.hasBlockedUsers()) {
            this.goToUnblockedActivity.setVisibility(View.GONE);
        }
        goToUnblockedActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, UnblockUsers.class);
                startActivity(intent);
            }
        });
        goToUnblockedActivity.setTransformationMethod(null);
    }

    @Override
    protected void onStop() {
        super.onStop();

        String funnyStatusString = funnyStatus.getText().toString();
        if (!funnyStatusString.equals(myContact.getFunnyStatus())){
            isDirty = true;
            myContact.setFunnyStatus(funnyStatusString);
        }

        if (!isDirty) {
            return;
        }

        ContactHelper.updateMyContact(MyProfileActivity.this);
        saveToServer();

        Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_toolbar, menu);
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
