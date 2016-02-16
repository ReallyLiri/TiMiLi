package com.android.callmemaybe.UI.data;

import android.net.Uri;

import com.android.callmemaybe.UI.R;
import com.android.callmemaybe.contracts.UserStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ana on 05/02/2016.
 */
public class Contact {
    public Uri imageUri; //the contact image
    public String userName; //the username the user chose for him
    public String phoneNumber;
    public UserStatus contactStatus;
    public int searchesCounter;
    public boolean hasApp;
    public boolean isFavorite = false;

    public Contact () {
        this("unnamed", "0");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserStatus getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(UserStatus contactStatus) {
        this.contactStatus = contactStatus;
    }

    public int getSearchesCounter() {
        return searchesCounter;
    }

    public void setSearchesCounter(int searchesCounter) {
        this.searchesCounter = searchesCounter;
    }

    public boolean isHasApp() {
        return hasApp;
    }

    public void setHasApp(boolean hasApp) {
        this.hasApp = hasApp;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isAvailable(){
        return false; //????
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

}
