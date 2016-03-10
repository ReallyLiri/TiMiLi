package com.android.callmemaybe.UI;

import android.view.View;
import android.widget.TextView;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.data.ContactFilter;
import com.android.callmemaybe.UI.data.ContactFilterType;
import com.android.callmemaybe.UI.data.ContactSort;
import com.android.callmemaybe.UI.data.ContactSortOrderType;
import com.android.callmemaybe.helpers.ContactHelper;

/**
 * Created by user on 22/02/2016.
 */
public class AllContactsFragment extends TabsFragment {
    @Override
    int getGreetingMessege() {
        return R.string.greeting_all;
    }

    @Override
    Contact[] getContacts() {
        Contact[] allContacts = ContactFilter.filterContacts(ContactFilterType.allValidContacts, getContext());
        return  ContactSort.sortContacts(ContactSortOrderType.name_A_To_Z, allContacts);
    }
}
