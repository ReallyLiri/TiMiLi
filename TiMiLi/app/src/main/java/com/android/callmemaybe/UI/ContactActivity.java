package com.android.callmemaybe.UI;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

<<<<<<< HEAD
import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.helpers.ContactHelper;
=======
import com.android.callmemaybe.UI.databinding.ContactDetailBinding;
>>>>>>> 3b0fe5e9a733dd4db42b946f4581b90fb0059c81

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.contact_detail);
        Toolbar myContactToolbar = binding.contactDetailToolbar;
        setSupportActionBar(myContactToolbar);

        String contactPhone = savedInstanceState.getString("PHONE_NUMBER");
        Contact currentContact = ContactHelper.getContact(contactPhone);

        myContactToolbar.setTitle(currentContact.getUserName());

        if (savedInstanceState == null) {
            // Create the contact detail fragment and add it to the activity
            // using a fragment transaction.
            ContactActivityFragment contactActivityFragment = new ContactActivityFragment();

            Bundle arguments = new Bundle();
            arguments.putString("PHONE_NUMBER", savedInstanceState.getString("PHONE_NUMBER"));

            ContactActivityFragment fragment = new ContactActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contact_detail_fragment, fragment, "ContactActivityFragmentTag")
                    .addToBackStack(null).commit();

        }
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
            case R.id.action_favorite:
                // we need to find the contact,
                // change the isFavorite field and update the symbol and the tables
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
