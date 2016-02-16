package com.android.callmemaybe.UI.data;

import android.net.Uri;

import com.android.callmemaybe.UI.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ana on 05/02/2016.
 */
public class Contact {
    public Uri imageUri; //the contact image
    public String userName; //the username the user chose for him
    public String phoneNumber;
    public int status; //the id of the string source
    public int photoId;
    public int searchesCounter;
    public boolean hasApp;
    public boolean isFavorite = false;

    public Contact () {
        this("unnamed", "0");
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass().getSimpleName().equals(this.getClass().getSimpleName())){
            Contact other = (Contact) o;
            return (this.phoneNumber.equals(other.phoneNumber));
        }
        else {
            return super.equals(o);
        }
    }

    public Contact(Uri imageUri, String name, String phoneNumber){
        this(name, phoneNumber);
        this.imageUri = imageUri;
    }

    /*
    checks with server if contact has app & update field
     */
    public boolean hasApp(){
        return false;
    }

    public Contact (String name, String number) {
        userName = name;
        phoneNumber = number;
        searchesCounter = 0;
    }

    public void addToFavorites () {
        if (isFavorite) {
            //announce contact already in favorites
        } else {
            isFavorite = true;
            //update tables
        }
    }

    public void deleteFromFavorites () {
        if (!isFavorite) {
            //announce contact in not in favorites
        } else {
            isFavorite = false;
            //update tables
        }
    }

    public String getUserName () {
        return userName;
    }

    public void setContactName (String name) {
        userName = name;
    }

    public int getStatus () {
        return status;
    }


    public void setStatus (String userStatus) {
        switch (userStatus) {
            case ("available") :
                status = R.string.available;
                break;
            case ("busy") :
                status = R.string.busy;
                break;
            case ("bored") :
                status = R.string.bored;
                break;
            case ("only_writing") :
                status = R.string.only_writing;
                break;
            default:
                status = R.string.default_status;
        }
    }

    public void setPhoto (int photo) {
        photoId = photo;
    }

    public int getPhotoId () {
        return photoId;
    }

    public String getPhoneNumber () {
        return phoneNumber;
    }

    public static List<Contact> dummyContacts() {
        List<Contact> contacts;
        Contact contact1 = new Contact("Liri", "04-0000000");
        contact1.setStatus("busy");
        contact1.setPhoto(R.drawable.art_clear);

        Contact contact2 = new Contact("Mia", "04-0000001");
        contact2.setStatus("bored");
        contact2.setPhoto(R.drawable.art_clear);

        Contact contact3 = new Contact("Tiani", "04-0000002");
        contact3.setStatus("available");
        contact3.setPhoto(R.drawable.art_clear);

        contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);

        return contacts;
    }
}
