package com.android.callmemaybe.timili.data;

import java.util.List;

/**
 * Created by Ana on 05/02/2016.
 */
public class AllContacts {

    //we need to decide how we will handle the contact information
    List<Contact> allContacts;

    private void addContact (String name, int number) {
        Contact contact = new Contact(name, number);
        //allContacts.insert whatever
    }

    public boolean isInContacts(String name) {
        return false;
    }

    public boolean isInContacts(int number) {
        return false;
    }

    public List<Contact> sortContacts (String sortOrder) {

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

        return allContacts;
    }

    public List<Contact> filterContracts () {
        return allContacts;
    }

    //when the user opens the app for the first time, the contact list needs to be uploaded
    public void bulkInsert() {

    }

    //find the 3 top searches contacts
    public Contact[] topSearched () {
        Contact[] top = new Contact[3];

        return top;
    }
}
