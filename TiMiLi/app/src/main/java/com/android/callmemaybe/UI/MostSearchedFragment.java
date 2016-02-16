package com.android.callmemaybe.UI;

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

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.data.ContactAdapter;
import com.android.callmemaybe.UI.data.ContactSort;
import com.android.callmemaybe.UI.data.ContactSortOrderType;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.helpers.TelephonyHelper;

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
        Contact[] top = ContactSort.sortContacts(ContactSortOrderType.mostSearchedToLeastSearched, ContactHelper.getAllContacts());
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

                //adding the phone number as extra in order to know who was chosen
                TextView phoneView = (TextView)
                        view.findViewById(R.id.most_searched_item_phone_textview);
                String phoneNumber = TelephonyHelper.normalizePhoneNumber
                        (phoneView.getText().toString());
                goToContactActivity.putExtra("Phone_Number", phoneNumber);

                startActivity(goToContactActivity);

            }
        });
        Log.d(LOG_TAG, "after setting onItemClickListener");

        return rootView;
    }
}
