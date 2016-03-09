package com.android.callmemaybe.helpers;
import android.content.ContentResolver;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.android.callmemaybe.UI.data.Contact;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static android.provider.ContactsContract.*;

/**
 * Created by user on 13/02/2016.
 */
public class ContactHelper {

    private static Set<Contact> allContacts;
    private static Contact myContact;
    public static final String CONTACTS_PREF_KEY = "ALL_CONTACTS";
    public static final String LOG_TAG = "ContactHelper";
    public static final String MY_CONTACT_PREF_KEY = "MY_CONTACT_PREF_KEY";

    public static Set<Contact> getAllContacts(){
        return allContacts;
    }


    /*
    get from phone contact list.
    for each contact read display name, phone num, pic
    check if this is a valid phone num
    if contact has multiple phone num - what should we do?

     */

    public static Contact getMyContact(Context context){
        if (myContact == null){
            SharedPreferencesHelper helpi = new SharedPreferencesHelper();
            try {
               myContact = helpi.getMyContact(context, MY_CONTACT_PREF_KEY);
            }
            catch (Exception e){
                PhoneNumberHelper helper = new PhoneNumberHelper();
                String phone = helper.getMyPhoneNumber(context);
                myContact = Contact.createMyContact(phone);
                helpi.putMyContact(context, myContact, MY_CONTACT_PREF_KEY);
            }
        }
        return myContact;
    }

    public static Set<Contact> getPhoneAllContacts(Context context){
        HashSet<Contact> contacts = new HashSet<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                Uri photoUri = null;
                String photoStr = cur.getString(cur.getColumnIndex(CommonDataKinds.Phone.PHOTO_URI));
                if (photoStr != null) {
                    photoUri = Uri.parse(photoStr);
                }
                String phoneNumber = null;
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phoneNumber = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    pCur.close();
                    contacts.add(new Contact(photoUri, name, phoneNumber));
                }

            }
        }
        return contacts;
    }

    public static void setAllContactPref(Context context, Set<Contact> contacts){
        SharedPreferencesHelper pref = new SharedPreferencesHelper();
        pref.PutAllContacts(context, contacts, CONTACTS_PREF_KEY);
    }


    public static void initAllContacts(Context context){
        setAllContactPref(context, getPhoneAllContacts(context));
        //& reg
    }


    /*
    gets image uri and prints the pic
     */
    public static Bitmap photoLoader(Uri image_uri, Context context){
            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(context.getContentResolver(),
                                image_uri);
                return bitmap;

            } catch (FileNotFoundException e) {
                Log.e(LOG_TAG, e.getMessage());
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            } catch (NullPointerException e){
                Log.e(LOG_TAG, e.getMessage());
            }
        return null;
    }

    /*
    check if there is a mismatch between the contact list saved in shared preferances & contact list on phone.
    if there is a user that exists only is shared pref - delete it from there
    if there is a user that exists only in contact list on phone - add to shared pref, check if has app $ register.
     */
    public static void updateContacts(Context context) {
        SharedPreferencesHelper pref = new SharedPreferencesHelper();

        Set<Contact> phoneContacts = getPhoneAllContacts(context);
        Set<Contact> prefContacts = pref.GetAllContacts(context, CONTACTS_PREF_KEY);

        Log.d("update Contacts", "phoneContactsContain " + phoneContacts.size() + " items");
        Log.d("update Contacts", "prefContactsContain " + (prefContacts == null));
       // for (Contact contact: phoneContacts) {
       //     Log.d("update Contacts",contact.getPhoneNumber());
       // }
       // for (Contact contact: prefContacts) {
       //     Log.d("update Contacts",contact.getPhoneNumber());
       // }


        if (prefContacts == null){
            Log.d("update Contacts", "prefContacts is null");
            pref.PutAllContacts(context, phoneContacts, CONTACTS_PREF_KEY);
            prefContacts = new HashSet<>();
            //bulk hasApp & bulk reg
        }

        for (Contact contact : phoneContacts){
            Log.d("update Contacts", "contact in phone Contacts " + contact.toString());
            if (!prefContacts.contains(contact)){
                Log.d("update Contacts", "pref Contacts does not contain " + contact.getUserName());
                prefContacts.add(contact);
                //reg & check if has app
            }
        }
        for (Contact contact :prefContacts){
            Log.d("update Contacts", "contact in pref Contacts " + contact.toString());
            if (!phoneContacts.contains(contact)){
                Log.d("update Contacts", "phone Contacts does not contain " + contact.getUserName());
                   prefContacts.remove(contact);
            }
        }
        pref.PutAllContacts(context, prefContacts, CONTACTS_PREF_KEY);
        allContacts = prefContacts;
    }

    //receives a phone number string
    //returns the first contact with the same phone number
    //or null if no match is found
    public static Contact getContact (String number) {
        Iterator<Contact> iterator = getAllContacts().iterator();
        Contact contact;
        while(iterator.hasNext()) {
            contact = iterator.next();
            if (number == contact.getPhoneNumber()) {
                return contact;
            }
        }
        return null;
    }

    //returns true if a contact with the given number exists
    //and false otherwise
    public static boolean containsContact (String number) {
        Contact contact = getContact(number);
        return (contact != null);
    }


}
