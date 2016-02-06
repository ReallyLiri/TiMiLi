package com.android.callmemaybe.timili.data;

/**
 * Created by Ana on 05/02/2016.
 */
public class Contact {
    public String userName; //the username the user chose for him
    public int phoneNumber;
    public String status;
    public int searchesCounter;
    public boolean hasApp;
    public boolean isFavorite = false;

    public Contact () {
        this("unnamed", 0);
    }

    public Contact (String name, int number) {
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

    public String getStatus () {
        return status;
    }
}
