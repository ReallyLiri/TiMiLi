package com.android.callmemaybe.UI.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.android.callmemaybe.UI.BR;
import com.android.callmemaybe.UI.R;
import com.android.callmemaybe.contracts.ActiveInPractice;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.contracts.UserStatus;
import com.android.callmemaybe.helpers.PhoneNumberHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ana on 05/02/2016.
 */
public class Contact extends BaseObservable {
    public Uri imageUri; //the contact image
    public String userName; //the username the user chose for him
    public String phoneNumber;
    public UserStatus contactStatus = new UserStatus(phoneNumber);
    public String status = "debugg";
    public UserGist contactGist = null;
    public int searchesCounter;
    public boolean hasApp;
    public boolean isFavorite = false;
    public String profileString = contactStatus.funnyStatus;

    public Contact () {
        this("unnamed", "0");
    }

    public void setProfileString(String profileString) {
        this.profileString = profileString;
    }

    public UserStatus getContactStatus() {
        return contactStatus;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        notifyPropertyChanged(BR.imageUri);
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    public void setContactStatus(UserStatus contactStatus) {
        this.contactStatus = contactStatus;
    }


    public void setStatus(String contactStatus) {
        Log.d("Contact" ,"this func is for debugging only!");
    }

    @Bindable
    public String getActiveInPractice() {
        return this.contactGist != null ?
                  this.contactGist.IsActiveInPractice(PhoneNumberHelper.GetMyPhoneNumber(), this.contactStatus).toString()
                : ActiveInPractice.Loading.toString();
    }

    public void setContactGist(UserGist contactGist) {
        this.contactGist = contactGist;
        notifyPropertyChanged(BR.activeInPractice);
    }

    @Bindable
    public int getSearchesCounter() {
        return searchesCounter;
    }

    public void setSearchesCounter(int searchesCounter) {
        this.searchesCounter = searchesCounter;
        notifyPropertyChanged(BR.searchesCounter);
    }

    public boolean isHasApp() {
        return hasApp;
    }

    public void setHasApp(boolean hasApp) {
        this.hasApp = hasApp;
    }

    @Bindable
    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
        notifyPropertyChanged(BR.isFavorite);
    }

    public void toggleFavorite() {
        this.isFavorite = !this.isFavorite;
        notifyPropertyChanged(BR.isFavorite);
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

    @Override
    public int hashCode() {
        return phoneNumber != null ? phoneNumber.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "imageUri=" + imageUri +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", contactStatus=" + contactStatus +
                ", searchesCounter=" + searchesCounter +
                ", hasApp=" + hasApp +
                ", isFavorite=" + isFavorite +
                '}';
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

    public boolean isInBlockedList(Contact contact){
        return this.contactStatus.blockedUsers.contains(contact);
    }
}
