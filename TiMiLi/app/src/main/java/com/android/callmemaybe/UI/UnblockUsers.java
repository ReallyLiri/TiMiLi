package com.android.callmemaybe.UI;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.callmemaybe.UI.data.BlockedContactAdapter;
import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.data.ContactAdapter;
import com.android.callmemaybe.UI.data.ContactFilter;
import com.android.callmemaybe.UI.data.ContactFilterType;
import com.android.callmemaybe.UI.data.ContactSort;
import com.android.callmemaybe.UI.data.ContactSortOrderType;
import com.android.callmemaybe.UI.databinding.ActivityUnblockUsersBinding;
import com.android.callmemaybe.UI.databinding.TabsFragmentBinding;

public class UnblockUsers extends AppCompatActivity {

    ListView cListView;
    ContactAdapter contactAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUnblockUsersBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_unblock_users);
        cListView = binding.blockedContacts;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //allows up navigation

        //needs to deal with the cases of no contacts at all or less then 3 contacts
        Contact[] blocked = getContacts();
        BlockedContactAdapter contactAdapter = new BlockedContactAdapter(UnblockUsers.this, blocked);
        cListView.setAdapter(contactAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_toolbar, menu);
        toolbar.setTitle(R.string.unblock);
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

    private Contact[] getContacts() {
        Contact[] res = ContactFilter.filterContacts(ContactFilterType.blocked, this);
        return ContactSort.sortContacts(ContactSortOrderType.name_A_To_Z, res);
    }

}
