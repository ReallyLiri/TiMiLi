package com.android.callmemaybe.timili.data;

import java.util.List;

/**
 * Created by Ana on 05/02/2016.
 */
public class AllContacts {

    //we need to decide how we will handle the contact information
    public static List<Contact> allContacts = Contact.dummyContacts();

    private static void addContact (String name, String number) {
        Contact contact = new Contact(name, number);
        //allContacts.insert whatever
    }

    public static boolean isInContacts(String name) {
        return false;
    }

    public static boolean isInContacts(int number) {
        return false;
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



}
