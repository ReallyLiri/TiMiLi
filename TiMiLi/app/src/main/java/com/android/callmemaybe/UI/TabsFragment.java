package com.android.callmemaybe.UI;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.data.ContactAdapter;
import com.android.callmemaybe.UI.data.ContactFilter;
import com.android.callmemaybe.UI.databinding.TabsFragmentBinding;

/**
 * Created by Mia on 22/02/2016.
 */
abstract class TabsFragment extends Fragment {

    private final String LOG_TAG = TabsFragment.class.getSimpleName();

    TextView greetingMassage;
    ListView cListView;
    ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TabsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.tabs_fragment, container, false);
        greetingMassage = binding.greetingMassage;
        cListView = binding.itemsList;

        //needs to deal with the cases of no contacts at all or less then 3 contacts
        greetingMassage.setText(getGreetingMessege());
        Contact[] top = getContacts();
        contactAdapter = new ContactAdapter(getActivity(), top);
        cListView.setAdapter(contactAdapter);

        cListView.setOnItemClickListener(getOnListItemClickedListener());
        Log.d(LOG_TAG, "after setting onItemClickListener");

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.RefreshData();
    }

    protected AdapterView.OnItemClickListener getOnListItemClickedListener() {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Contact selected = contactAdapter.getItem(position);
                ContactActivity.StartContactActivity(getActivity(), selected.getPhoneNumber());
            }
        };
    }

    public void RefreshData() {
        if (contactAdapter != null) {
            contactAdapter.clear();
            contactAdapter.addAll(getContacts());
            contactAdapter.notifyDataSetChanged();
        }
    }

    abstract int getGreetingMessege();
    abstract Contact[] getContacts();
}
