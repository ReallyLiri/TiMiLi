package com.android.callmemaybe.timili;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.callmemaybe.timili.data.AllContacts;
import com.android.callmemaybe.timili.data.Contact;
import com.android.callmemaybe.timili.data.ContactAdapter;

/**
 * Created by Ana on 05/02/2016.
 */
public class MostSearchedFragment extends Fragment {

    ListView cListView;
    ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.most_searched, container, false);
        cListView = (ListView) rootView.findViewById(R.id.most_searched_listview);

        //needs to deal with the cases of no contacts at all or less then 3 contacts
        Contact[] top = AllContacts.topSearched();
        contactAdapter = new ContactAdapter(getActivity(), top);

        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Intent goToContactActivity = new Intent(getContext(), ContactActivity.class);
                startActivity(goToContactActivity);

            }
        });

        return rootView;
    }
}
