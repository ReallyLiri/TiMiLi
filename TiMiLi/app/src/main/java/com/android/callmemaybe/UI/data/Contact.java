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
    public String imageUri; //the contact image
    public String userName; //the username the user chose for him
    public String phoneNumber;
    public UserStatus contactStatus = new UserStatus(phoneNumber);
    public UserGist contactGist = null;
    public int searchesCounter;
    public boolean hasApp;
    public boolean isFavorite = false;

    private final String LOG_TAG = Contact.class.getSimpleName();
    private static final String THIS_USER_NAME = "myName";

    public static Contact createMyContact(String phoneNumber){
        Contact contact = new Contact(THIS_USER_NAME, phoneNumber);
        return contact;
    }

    public boolean[] getBlockedDays(){
        boolean[] res = new boolean[7];
        for (Integer i = 1; i < 8; i++){
            if (this.contactStatus.inactiveDays.contains(i)){
                res[i-1] = true;
            }
            else {
                res[i - 1] = false;
            }
        }
        return res;
    }

    public Contact () {
        this("unnamed", "0");
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    @Bindable
    public String getFunnyStatus(){
        return this.contactStatus.funnyStatus;
    }

    public void setFunnyStatus(String funnyStatus) {
        this.contactStatus.funnyStatus = funnyStatus;
        notifyPropertyChanged(BR.funnyStatus);
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public Uri getImageUri() {
        return imageUri == null ? null : Uri.parse(imageUri);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri.toString();
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

    @Bindable
    public UserStatus getContactStatus() {
        return this.contactStatus;
    }

    public void setContactStatus(UserStatus contactStatus) {
        this.contactStatus = contactStatus;
        notifyPropertyChanged(BR.contactStatus);
        notifyPropertyChanged(BR.funnyStatus);
        notifyPropertyChanged(BR.activeInPractice);
    }

    @Bindable
    public String getActiveInPractice() {
        return this.contactGist != null ?
                  this.contactGist.IsActiveInPractice(PhoneNumberHelper.GetMyPhoneNumber(), this.contactStatus).toString()
                : ActiveInPractice.Loading.toString();
    }

    @Bindable
    public int getActiveInPracticeDrawable() {
        if (this.contactGist == null) {
            return R.drawable.circle_gray;
        }
        switch (this.contactGist.IsActiveInPractice(PhoneNumberHelper.GetMyPhoneNumber(), this.contactStatus)) {
            case Active:
                return R.drawable.circle_green;
            case Inactive:
                return R.drawable.circle_yellow;
            case Unwilling:
                return R.drawable.circle_red;
            default:
                return R.drawable.circle_gray;
        }
    }

    public void setContactGist(UserGist contactGist) {
        this.contactGist = contactGist;
        if (this.contactStatus != null) {
            notifyPropertyChanged(BR.activeInPractice);
        }
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
        this.imageUri = imageUri == null ? null : imageUri.toString();
    }


    public Contact (String name, String number) {
        userName = name;
        phoneNumber = number;
        searchesCounter = 0;
        imageUri = null;
    }

    public boolean isInBlockedList(Contact contact){
        return this.contactStatus.blockedUsers.contains(contact.phoneNumber);
    }

    public boolean hasBlockedUsers(){
        return this.contactStatus.blockedUsers.size() > 0;
    }
}
