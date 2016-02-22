package com.android.callmemaybe.UI;

import android.util.Log;
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
public class MyFavoritesFragment extends TabsFragment {

    @Override
    int getGreetingMessege() {
        return R.string.greeting_favs;
    }

    @Override
    Contact[] getContacts() {
        Contact[] filtered = ContactFilter.filterContacts(ContactFilterType.favorites, ContactHelper.getAllContacts());
        Log.d("myFavs", filtered.length + " favs contact");
        return ContactSort.sortContacts(ContactSortOrderType.name_A_To_Z, filtered);
    }
}
