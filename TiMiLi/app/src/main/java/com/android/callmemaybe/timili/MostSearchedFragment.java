package com.android.callmemaybe.timili;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.callmemaybe.timili.data.AllContacts;
import com.android.callmemaybe.timili.data.Contact;
import com.android.callmemaybe.timili.data.ContactAdapter;

/**
 * Created by Ana on 05/02/2016.
 */
public class MostSearchedFragment extends Fragment {

    private final String LOG_TAG = MostSearchedFragment.class.getSimpleName();

    TextView greetingMassage;
    ListView cListView;
    ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.most_searched, container, false);
        greetingMassage = (TextView) rootView.findViewById(R.id.most_searched_greeting_massage);
        cListView = (ListView) rootView.findViewById(R.id.most_searched_listview);

        //needs to deal with the cases of no contacts at all or less then 3 contacts
        greetingMassage.setText(R.string.greeting);
        Contact[] top = AllContacts.topSearched();
        Log.d(LOG_TAG, "before creating new contactAdapter");
        contactAdapter = new ContactAdapter(getActivity(), top);
        Log.d(LOG_TAG, "after creating new contactAdapter");
        cListView.setAdapter(contactAdapter);
        Log.d(LOG_TAG, "after setting the contactAdapter");

        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //needs to go to the ContactActivity of the chosen contact
                ContactActivity contactActivity = new ContactActivity();
                Intent goToContactActivity = new Intent(getContext(), ContactActivity.class);
                //not sure how I'm supposed to pass the contact chosen
                startActivity(goToContactActivity);

            }
        });
        Log.d(LOG_TAG, "after setting onItemClickListener");

        return rootView;
    }
}
