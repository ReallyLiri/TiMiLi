package com.android.callmemaybe.UI.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;


/**
 * Created by Ana on 09/03/2016.
 */
public class ButtonAction {
    Contact contact;

    public ButtonAction(Contact currContact) {
        contact = currContact;
    }

    public void favAction (Context context, View v) {
        contact.toggleFavorite();
        //change picture
    }

    public void blockAction (final Context context, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to block this user?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //TODO: update server that this user is blocked
                Toast.makeText(context, "this user is blocked!!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void msgAction (Context context) {
        String uri = "tel:" + contact.getPhoneNumber();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(uri));
        Intent chooser = Intent.createChooser(intent, "Send message with...");
        context.startActivity(chooser);
    }

    public void callAction (Context context) {
        String uri = "tel:" + contact.getPhoneNumber();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }

}
