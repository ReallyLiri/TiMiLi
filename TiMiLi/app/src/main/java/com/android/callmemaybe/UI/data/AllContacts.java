package com.android.callmemaybe.UI.data;

import com.android.callmemaybe.helpers.TelephonyHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by Ana on 05/02/2016.
 */
public class AllContacts {

    //we need to decide how we will handle the contact information
    //for now we won't delete contacts from the list in order to keep their indexes
    public static List<Contact> allContacts = Contact.dummyContacts();//change to map in all code :)
    public static Map<String, Contact> allContactsMap;

    private static void addContact (String name, String number) {
        Contact contact = new Contact(name, number);
        allContactsMap.put(TelephonyHelper.normalizePhoneNumber(number), contact);
    }

    //add later: public static boolean isInContacts(String name)


    public static boolean isInContacts(String number) {
        return allContactsMap.containsKey(TelephonyHelper.normalizePhoneNumber(number));
    }

    public static List<Contact> sortContacts (String sortOrder) {

        /**
        switch (sortOrder) {
            case(R.string.app_name) :
                return Collections.sort(allContacts, Contact.getUserName());
                break;
            case (R.string.availability) :
                return Collections.sort(allContacts, Contact.getStatus());
                break;
        }
         **/

        return AllContacts.allContacts;
    }

    public static List<Contact> filterContracts () {
        return allContacts;
    }

    //when the user opens the app for the first time, the contact list needs to be uploaded
    public void bulkInsert() {

    }

    //find the 3 top searches contacts
    public static Contact[] topSearched () {
        List<Contact> topList;
        Contact[] topArray;
        if (allContacts.size() <= 3) {
            topList = allContacts;
            topArray = new Contact[topList.size()];
        } else {
            topList = sortContacts("searchedCounter");
            topList = topList.subList(0, 3);
            topArray = new Contact[3];
        }
        for (int i = 0; i < topArray.length; i++) {
            topArray[i] = topList.get(i);
        }
        return topArray;
    }

    public static int getAllContactsSize () {
        return allContactsMap.size();
    }


}
