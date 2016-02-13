package com.android.callmemaybe.helpers;

import com.android.callmemaybe.UI.data.Contact;

import java.util.HashSet;

/**
 * Created by user on 13/02/2016.
 */
public class ContactHelper {

    /*
    get from phone contact list.
    for each contact read display name, phone num, pic
    check if this is a valid phone num
    if contact has multiple phone num - what should we do?

     */
    public static HashSet<Contact> getPhoneContactList(){
        return null;
    }

    /*
    check if there is a mismatch between the contact list saved in shared preferances & contact list on phone.
    if there is a user that exsist only is shared pref - delete it from there
    if there is a user that exsist only in contact list on phone - add to shared pref, check if has app $ register.
     */
    public static void updateContacts(){

    }
}
