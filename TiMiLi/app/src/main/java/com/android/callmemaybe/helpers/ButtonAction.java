package com.android.callmemaybe.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.android.callmemaybe.UI.R;
import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.contracts.UserStatus;
import com.android.callmemaybe.notificationService.NotificationService;

import java.util.List;


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

    public void blockAction (final Context context, View v, DialogInterface.OnClickListener onPositiveButtonClicked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to block this user?");

        builder.setPositiveButton("OK", onPositiveButtonClicked);

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
        Intent chooser = Intent.createChooser(intent, "Call with...");
        context.startActivity(chooser);
    }

    public static void addContactToPhone (Context context) {
        Intent goToContactsApp = new Intent(Intent.ACTION_INSERT,
                ContactsContract.Contacts.CONTENT_URI);
        goToContactsApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(goToContactsApp);
    }

    public static void trackAction(final Context context, final Contact contactToTrack, final CoordinatorLayout coordinatorLayout) {
        Contact myContact = ContactHelper.getMyContact(context);
        UserStatus myStatus = myContact.getContactStatus();
        List<String> currTrackedList = myStatus.trackedUsers;

        if (contactToTrack.getIsTracked()) {
            contactToTrack.setIsTracked(false);
            if (currTrackedList.contains(contactToTrack.getPhoneNumber())) {
                currTrackedList.remove(contactToTrack.getPhoneNumber());
            }

            showTrackSnackbar(false, context, contactToTrack, coordinatorLayout);
        } else {
            contactToTrack.setIsTracked(true);
            if (!currTrackedList.contains(contactToTrack.getPhoneNumber())) {
                currTrackedList.add(contactToTrack.getPhoneNumber());
            }

            showTrackSnackbar(true, context, contactToTrack, coordinatorLayout);
        }
        ContactHelper.updateMyContact(context);
        NotificationService.NotifyOfTrackedListChanged(context, currTrackedList);
    }

    private static void showTrackSnackbar(boolean isTrackedNow, final Context context, final Contact contactToTrack, CoordinatorLayout coordinatorLayout) {
        if (coordinatorLayout != null) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout,
                            contactToTrack.getUserName() + " " + (isTrackedNow ? context.getString(R.string.contact_is_tracked) : context.getString(R.string.contact_is_untracked)),
                            Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            trackAction(context, contactToTrack, null);
                        }
                    });
            snackbar.show();
        }
    }
}
