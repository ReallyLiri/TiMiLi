package com.android.callmemaybe.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.callmemaybe.UI.data.Contact;

import java.util.Set;

/**
 * Created by Liri on 05/02/2016.
 */
public class SharedPreferencesHelper {

    private static final String IDENTITY = "com.android.callmemaybe.timili";

    public boolean getShowOnlyActive (Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, true);
    }

    public void putShowOnlyActive(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(key, value).apply();
    }

    public void putMyContact(Context context, Contact contact, String key){
        ComplexPreferences prefs = ComplexPreferences.getComplexPreferences(context, IDENTITY, Context.MODE_PRIVATE);
        prefs.putObject(key, contact);
        prefs.commit();
    }

    public Contact getMyContact(Context context, String key){
        ComplexPreferences prefs = ComplexPreferences.getComplexPreferences(context, IDENTITY, Context.MODE_PRIVATE);
        Contact contact = prefs.getObject(key, Contact.class);
        if (contact == null) {
            return null;
        }
        return contact;
    }

    public void PutAllContacts(Context context, Set<Contact> allContacts, String key){
        ComplexPreferences prefs = ComplexPreferences.getComplexPreferences(context, IDENTITY, Context.MODE_PRIVATE);
        ContactsSet set = new ContactsSet(allContacts);
        prefs.putObject(key, set);
        prefs.commit();
    }

    /*
    return AllContacts in Pref.
    important! if Pref[key] doesn't exsist - return null!
     */
    public Set<Contact> GetAllContacts(Context context, String key){
        ComplexPreferences prefs = ComplexPreferences.getComplexPreferences(context, IDENTITY, Context.MODE_PRIVATE);
        ContactsSet set = prefs.getObject(key, ContactsSet.class);
        if (set == null) {
            return null;
        }
        return set.set;
    }

    private class ContactsSet {
        public Set<Contact> set;
        public ContactsSet(Set<Contact> set) {
            this.set = set;
        }
    }

    public String GetString(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public void PutString(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        prefs.edit().putString(key, value).apply();
    }

    public Set<String> GetStringSet(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        return prefs.getStringSet(key, null);
    }

    public void PutStringSet(Context context, String key, Set<String> value) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(key, value).apply();
    }

    public<T extends Enum<T>> T GetEnum(Context context, String key, Class enumClass, T defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        String name = prefs.getString(key, null);
        if (name == null) {
            return defaultValue;
        }
        return (T) Enum.valueOf(enumClass, name);
    }

    public<T extends Enum<T>> void PutEnum(Context context, String key, T value) {
        SharedPreferences prefs = context.getSharedPreferences(IDENTITY, Context.MODE_PRIVATE);
        prefs.edit().putString(key, value.name()).apply();
    }

}
