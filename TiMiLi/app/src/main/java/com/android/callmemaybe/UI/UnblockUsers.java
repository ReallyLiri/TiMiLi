package com.android.callmemaybe.UI;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUnblockUsersBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_unblock_users);
        cListView = binding.blockedContacts;

        //needs to deal with the cases of no contacts at all or less then 3 contacts
        Contact[] blocked = getContacts();
        BlockedContactAdapter contactAdapter = new BlockedContactAdapter(UnblockUsers.this, blocked);
        cListView.setAdapter(contactAdapter);
    }



    private Contact[] getContacts() {
        Contact[] res = ContactFilter.filterContacts(ContactFilterType.blocked, this);
        return ContactSort.sortContacts(ContactSortOrderType.name_A_To_Z, res);
    }

}
