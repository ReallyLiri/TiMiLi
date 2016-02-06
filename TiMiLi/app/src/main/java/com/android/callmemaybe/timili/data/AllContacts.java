package com.android.callmemaybe.timili.data;

import com.android.callmemaybe.timili.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ana on 05/02/2016.
 */
public class AllContacts {

    //we need to decide how we will handle the contact information
    List<Contact> allContacts;

    public AllContacts () {
        Contact contact1 = new Contact("Liri", "04-0000000");
        contact1.setStatus("busy");
        contact1.setPhoto(R.drawable.art_clear);

        Contact contact2 = new Contact("Mia", "04-0000001");
        contact2.setStatus("bored");
        contact1.setPhoto(R.drawable.art_clear);

        Contact contact3 = new Contact("Tiani", "04-0000002");
        contact3.setStatus("available");
        contact1.setPhoto(R.drawable.art_clear);

        allContacts = new ArrayList<>();
        allContacts.add(contact1);
        allContacts.add(contact2);
        allContacts.add(contact2);
    }

    private void addContact (String name, String number) {
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
    public List<Contact> topSearched () {
        if (allContacts.size() <= 3) {
            return allContacts;
        } else {
            List<Contact> top = sortContacts("searchedCounter");
            return top.subList(0, 3);
        }
    }



}
