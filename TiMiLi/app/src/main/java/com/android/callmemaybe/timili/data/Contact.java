package com.android.callmemaybe.timili.data;

import com.android.callmemaybe.timili.R;

/**
 * Created by Ana on 05/02/2016.
 */
public class Contact {
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
}
