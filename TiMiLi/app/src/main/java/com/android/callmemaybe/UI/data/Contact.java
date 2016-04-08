package com.android.callmemaybe.UI.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.android.callmemaybe.UI.BR;
import com.android.callmemaybe.UI.R;
import com.android.callmemaybe.contracts.ActiveInPractice;
import com.android.callmemaybe.contracts.UserGist;
import com.android.callmemaybe.contracts.UserStatus;
import com.android.callmemaybe.helpers.PhoneNumberHelper;

/**
 * Created by Ana on 05/02/2016.
 */
public class Contact extends BaseObservable {
    private String imageUri; //the contact image
    private String userName; //the username the user chose for him
    private String phoneNumber;
    private UserStatus contactStatus = new UserStatus(phoneNumber);
    private UserGist contactGist = null;
    private int searchesCounter;
    private boolean isFavorite = false;
    private boolean isTracked = false;

    private final String LOG_TAG = Contact.class.getSimpleName();
    private static final String THIS_USER_NAME = "myName";

    public static Contact createMyContact(String phoneNumber){
        Contact contact = new Contact(THIS_USER_NAME, phoneNumber);
        contact.contactStatus.phoneNum = phoneNumber;
        return contact;
    }

    @Bindable
    public boolean[] getBlockedDays(){
        boolean[] days = new boolean[8];
        for (int i = 1; i<=7; i++) {
            days[i] = this.contactStatus.inactiveDays.contains(i);
        }
        return days;
    }

    public void toggleBlockedDay(Integer i){
        if (this.contactStatus.inactiveDays.contains(i)) {
            this.contactStatus.inactiveDays.remove(i);
        }
        else {
            this.contactStatus.inactiveDays.add(i);
        }
        notifyPropertyChanged(BR.blockedDays);
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
        Log.d("Contact", "status is " + this.contactStatus + " and I'm " + userName);
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
        this.contactStatus.phoneNum = phoneNumber;
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

    public int getActiveInPracticeDrawable() {

        if (this.contactGist == null || this.contactStatus == null) {
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

    @Bindable
    public boolean getIsTracked() {
        return isTracked;
    }

    public void setIsTracked(boolean isTracked) {
        this.isTracked = isTracked;
        notifyPropertyChanged(BR.isTracked);
    }

    public void toggleTracked() {
        this.isTracked = !this.isTracked;
        notifyPropertyChanged(BR.isTracked);
    }

    public boolean isAvailable(){
        return getActiveInPractice().equals(ActiveInPractice.Active.toString());
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
                ", isFavorite=" + isFavorite +
                ", isTracked=" + isTracked +
                '}';
    }

    public Contact(Uri imageUri, String name, String phoneNumber){
        this(name, phoneNumber);
        this.imageUri = imageUri == null ? null : imageUri.toString();
        contactStatus = new UserStatus(phoneNumber);
    }


    public Contact (String name, String number) {
        userName = name;
        phoneNumber = number;
        searchesCounter = 0;
        imageUri = null;
        contactStatus = new UserStatus(number);
    }

    public boolean isInBlockedList(Contact contact){
        return this.contactStatus.blockedUsers.contains(contact.phoneNumber);
    }

    public boolean hasBlockedUsers(){
        return this.contactStatus.blockedUsers.size() > 0;
    }


}
