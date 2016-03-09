package com.android.callmemaybe.UI.data;

import android.view.View;

/**
 * Created by Ana on 09/03/2016.
 */
public class ButtonAction {
    Contact contact;

    public ButtonAction(Contact currContact) {
        contact = currContact;
    }

    public void favAction (View v) {
        contact.toggleFavorite();
        //change picture
    }

    public void blockAction (View v) {


    }


}
