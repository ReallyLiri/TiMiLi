package com.android.callmemaybe.UI.data;

import android.app.Activity;
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
import com.android.callmemaybe.UI.MainActivity;
import com.android.callmemaybe.UI.databinding.BlockedContactListItemBinding;
import com.android.callmemaybe.UI.databinding.ContactListItemBinding;
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mia on 10/03/2016.
 */
public class BlockedContactAdapter extends ArrayAdapter<Contact> {

    private ICloudServer mServer;

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
        super(context, 0, new ArrayList<>(Arrays.asList(contacts)));
        mServer = new FireBaseCloudServer(context);
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
                ContactActivity.StartContactActivity(getContext(), phone, "BlockedContacts");
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
                        Contact myContact = ContactHelper.getMyContact(getContext());
                        myContact.getContactStatus().blockedUsers.remove(contact.getPhoneNumber());

                        mServer.UpdateMyStatus(myContact.getContactStatus());

                        BlockedContactAdapter.this.remove(contact);
                        BlockedContactAdapter.this.notifyDataSetChanged();

                        ContactHelper.updateMyContact(getContext());

                        MainActivity.refreshAllData();

                        Toast.makeText(getContext(), "this user is unblocked!!", Toast.LENGTH_LONG).show();

                        if (BlockedContactAdapter.this.getCount() == 0) {
                            ((Activity) BlockedContactAdapter.this.getContext()).finish();
                        }

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

