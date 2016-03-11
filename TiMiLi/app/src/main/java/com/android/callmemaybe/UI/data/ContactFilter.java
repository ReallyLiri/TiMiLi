package com.android.callmemaybe.UI.data;

import android.content.Context;

import com.android.callmemaybe.helpers.ContactHelper;
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


    public static Contact[] filterContacts(ContactFilterType filterType, Context context) {
        Set<Contact> contacts = ContactHelper.getAllContacts();
        Contact myContact1 = ContactHelper.getMyContact(context);
        List<Contact> result = new ArrayList<>();
        for (Contact contact: contacts) {
            switch (filterType){
                case allValidContacts:
                    if (!myContact1.isInBlockedList(contact)){
                        result.add(contact);
                    }
                    break;
                case favorites:
                    if (contact.getIsFavorite() && !myContact1.isInBlockedList(contact)) {
                        result.add(contact);
                    }
                    break;
                case available:
                    if (contact.isAvailable() && !myContact1.isInBlockedList(contact)){
                        result.add(contact);
                    }
                    break;
                case blocked:
                    if (myContact1.isInBlockedList(contact)){
                        result.add(contact);
                    }
                    break;
            }
        }
        Contact[] resultArr = new Contact[result.size()];
        resultArr = result.toArray(resultArr);
        return resultArr;
    }

    public static Contact[] filterContacts(String word, Context context){
        word = word.toLowerCase();
        Set<Contact> contacts = ContactHelper.getAllContacts();
        Contact myContact1 = ContactHelper.getMyContact(context);
        List<Contact> result = new ArrayList<>();
        for (Contact contact: contacts){
            if (contact.getPhoneNumber().contains(word) || contact.getUserName().toLowerCase().contains(word)){
                result.add(contact);
            }
        }
        Contact[] resultArr = new Contact[result.size()];
        resultArr = result.toArray(resultArr);
        return resultArr;
    }

    public static Contact[] filterContacts(String word, Contact[] contacts) {
        List<Contact> result = new ArrayList<>();
        for (Contact contact: contacts) {
            if (contact.getUserName().contains(word) ||
                    contact.getPhoneNumber().contains(word)) {
                result.add(contact);
            }
        }
        Contact[] resultArr = new Contact[result.size()];
        resultArr = result.toArray(resultArr);
        return resultArr;
    }
}
