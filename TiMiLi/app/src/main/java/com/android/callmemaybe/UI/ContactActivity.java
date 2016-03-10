package com.android.callmemaybe.UI;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

    public static final String PHONE_NUMBER_EXTRA = "PHONE_NUMBER";

    public static void StartContactActivity(Context context, String phoneNumber) {

        Intent goToContactActivity = new Intent(context, ContactActivity.class);

        goToContactActivity.putExtra(PHONE_NUMBER_EXTRA, phoneNumber);

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
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
