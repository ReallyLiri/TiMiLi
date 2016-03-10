package com.android.callmemaybe.UI;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.TaskStackBuilder;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.IOnLatestGistUpdatedListener;
import com.android.callmemaybe.contracts.IOnLatestStatusUpdatedListener;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.contracts.UserStatus;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.UI.databinding.ContactDetailBinding;
import com.android.callmemaybe.server.FireBaseCloudServer;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactActivity extends AppCompatActivity {

    private ICloudServer mCloudServer;
    private Contact mContact;
    private String callingActivity;
    private String searchString;

    public static final String PHONE_NUMBER_EXTRA = "PHONE_NUMBER";
    public static final String SEARCH_STRING = "SEARCH_STRING";

    public static void StartContactActivity(Context context, String phoneNumber) {

        Intent goToContactActivity = new Intent(context, ContactActivity.class);

        goToContactActivity.putExtra(PHONE_NUMBER_EXTRA, phoneNumber);
        goToContactActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(goToContactActivity);
    }

    public static void StartContactActivity(Context context, String phoneNumber, String searchWord) {

        Intent goToContactActivity = new Intent(context, ContactActivity.class);

        goToContactActivity.putExtra(PHONE_NUMBER_EXTRA, phoneNumber);
        goToContactActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        goToContactActivity.putExtra(SEARCH_STRING, searchWord);

        context.startActivity(goToContactActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mCloudServer = new FireBaseCloudServer(this);
        mCloudServer.RegisterForUserStatusData(mContact.getPhoneNumber(), new IOnLatestStatusUpdatedListener() {
            @Override
            public void latestStatusUpdated(UserStatus latestStatus) {
                mContact.setContactStatus(latestStatus);
            }
        });
        mCloudServer.RegisterForUserGistData(mContact.getPhoneNumber(), new IOnLatestGistUpdatedListener() {
            @Override
            public void latestGistUpdated(UserGist latestGist) {
                mContact.setContactGist(latestGist);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCloudServer.UnRegisterAll();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PHONE_NUMBER_EXTRA, mContact.getPhoneNumber());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContactDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.contact_detail);
        Toolbar myContactToolbar = binding.contactDetailToolbar;
        setSupportActionBar(myContactToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //allows up navigation

        callingActivity = getIntent().getStringExtra("CALLNG_ACTIVITY");
        if (callingActivity != "MainActivity") {
            searchString = getIntent().getStringExtra("SEARCH_STRING");
        }

        String contactPhone;
        if (savedInstanceState != null) {
            contactPhone = savedInstanceState.getString(PHONE_NUMBER_EXTRA);
        }
        else {
            contactPhone = getIntent().getStringExtra(PHONE_NUMBER_EXTRA);
        }

        mContact = ContactHelper.getContact(contactPhone);

        if (mContact == null) {
            Toast.makeText(this, "Failed to get contact data for number: " + contactPhone, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        binding.setContact(mContact);

        ContactActivityFragment fragment = new ContactActivityFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_contact, fragment, "ContactActivityFragmentTag")
                .addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_detail_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.

                    if (callingActivity == "MainActivity") {
                        NavUtils.navigateUpTo(this, upIntent);
                    } else {
                        upIntent = new Intent(getApplicationContext(), SearchActivity.class);
                        upIntent.putExtra(SEARCH_STRING, searchString);
                        NavUtils.navigateUpTo(this, upIntent);
                    }

                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
