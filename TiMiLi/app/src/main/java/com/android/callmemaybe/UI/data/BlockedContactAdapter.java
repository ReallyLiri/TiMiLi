package com.android.callmemaybe.UI.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.callmemaybe.UI.ContactActivity;
import com.android.callmemaybe.UI.databinding.BlockedContactListItemBinding;
import com.android.callmemaybe.UI.databinding.ContactListItemBinding;
import com.android.callmemaybe.helpers.ContactHelper;

/**
 * Created by Mia on 10/03/2016.
 */
public class BlockedContactAdapter extends ArrayAdapter<Contact> {
    private final String LOG_TAG = ContactAdapter.class.getSimpleName();
    private Contact[] contactsList;

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public BlockedContactListItemBinding contactListItemBinding;

        public ViewHolder(BlockedContactListItemBinding contactListItemBinding) {
            this.contactListItemBinding = contactListItemBinding;
        }
    }

    public BlockedContactAdapter(Context context, Contact[] contacts) {
        super(context, 0, contacts);
        contactsList = contacts;
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {


        final Contact contact = getItem(position);
        final ViewHolder holder;
        final String phone = contact.getPhoneNumber();

        if (convertView == null) {
            BlockedContactListItemBinding contactListItemBinding = BlockedContactListItemBinding
                    .inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = contactListItemBinding.getRoot();

            holder = new ViewHolder(contactListItemBinding);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.contactListItemBinding.setContact(contact);

        holder.contactListItemBinding.contactListItemUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactActivity.StartContactActivity(getContext(), phone);
            }
        });



        holder.contactListItemBinding.unblockedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogOpner();
            }

            private void dialogOpner() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to unblock this user?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Contact myContact1 = ContactHelper.getMyContact(getContext());
                        myContact1.contactStatus.blockedUsers.remove(contact.getPhoneNumber());
                        //TODO: update server that this user is blocked
                        ContactHelper.updateMyContact(getContext());
                        Toast.makeText(getContext(), "this user is unblocked!!", Toast.LENGTH_LONG).show();
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
        });

        convertView.setTag(holder);

        return convertView;
    }
}

