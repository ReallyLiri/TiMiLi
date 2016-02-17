package com.android.callmemaybe.UI.data;

import com.firebase.client.core.utilities.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 17/02/2016.
 */
public class ContactFilter {


    public static Contact[] filterContacts(ContactFilterType filterType, Set<Contact> contacts) {
        List<Contact> result = new ArrayList<>();
        for (Contact contact: contacts) {
            switch (filterType){
                case favorites:
                    if (contact.isFavorite()) {
                        result.add(contact);
                    }
                    break;
                case available:
                    if (contact.isAvailable()){
                        result.add(contact);
                    }
                    break;
            }
        }
        return (Contact[]) result.toArray();
    }

    public static Contact[]  filterContacts(ContactFilterType filterType, Contact[] contacts){
        Set<Contact> contacstSet = new HashSet<>();
        contacstSet.addAll(java.util.Arrays.asList(contacts));
        return filterContacts(filterType, contacstSet);
    }
}
