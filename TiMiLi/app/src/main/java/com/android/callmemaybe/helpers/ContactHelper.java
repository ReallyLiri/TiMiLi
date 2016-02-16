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
import java.util.Set;

/**
 * Created by user on 13/02/2016.
 */
public class ContactHelper {

    private static Set<Contact> allContacts;
    public static final String CONTACTS_PREF_KEY = "ALL_CONTACTS";
    public final String LOG_TAG = "ContactHelper";

    public static Set<Contact> getAllContacts(){
        return allContacts;
    }


    /*
    get from phone contact list.
    for each contact read display name, phone num, pic
    check if this is a valid phone num
    if contact has multiple phone num - what should we do?

     */
    public Set<Contact> getPhoneAllContacts(Context context){
        HashSet<Contact> contacts = new HashSet<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        while (cur.moveToNext())
        {
            String name=cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = TelephonyHelper.normalizePhoneNumber(cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            Uri imageUri = null;
            String stringImageUri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            if (stringImageUri != null) {
                Uri.parse(stringImageUri);
            }
            Contact contact = new Contact(imageUri, name, phoneNumber);
            contacts.add(contact);
        }
        cur.close();
        return contacts;
    }

    public void setAllContactPref(Context context, Set<Contact> contacts){
        SharedPreferencesHelper pref = new SharedPreferencesHelper();
        pref.PutAllContacts(context, contacts, CONTACTS_PREF_KEY);
    }

    public void initAllContacts(Context context){
        setAllContactPref(context, this.getPhoneAllContacts(context));
        //& reg
    }


    /*
    gets image uri and prints the pic
     */
    public Bitmap photoLoader(Uri image_uri, Context context){
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
    if there is a user that exsist only is shared pref - delete it from there
    if there is a user that exsist only in contact list on phone - add to shared pref, check if has app $ register.
     */
    public void updateContacts(Context context) {
        SharedPreferencesHelper pref = new SharedPreferencesHelper();

        Set<Contact> phoneContacts = this.getPhoneAllContacts(context);
        Set<Contact> prefContacts = pref.GetAllContacts(context, CONTACTS_PREF_KEY);

        if (prefContacts == null){
            pref.PutAllContacts(context, phoneContacts, CONTACTS_PREF_KEY);
            //bulk hasApp & bulk reg
        }

        for (Contact contact : phoneContacts){
            if (!prefContacts.contains(contact)){
                prefContacts.add(contact);
                //reg & check if has app
            }
        }
        for (Contact contact :prefContacts){
            if (!phoneContacts.contains(contact)){
                   prefContacts.remove(contact);
            }
        }
        pref.PutAllContacts(context, prefContacts, CONTACTS_PREF_KEY);
    }
}
