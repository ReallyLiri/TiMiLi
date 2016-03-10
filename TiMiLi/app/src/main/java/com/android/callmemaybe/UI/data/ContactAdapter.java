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
import com.android.callmemaybe.UI.MainActivity;
import com.android.callmemaybe.UI.databinding.ContactListItemBinding;
import com.android.callmemaybe.helpers.ButtonAction;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    private final String LOG_TAG = ContactAdapter.class.getSimpleName();
    private Contact[] contactsList;

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public ContactListItemBinding contactListItemBinding;

        public ViewHolder(ContactListItemBinding contactListItemBinding) {
            this.contactListItemBinding = contactListItemBinding;
        }
    }

    public ContactAdapter(Context context, Contact[] contacts) {
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
        final ButtonAction buttonAction = new ButtonAction(contact);

        if (convertView == null) {
            ContactListItemBinding contactListItemBinding = ContactListItemBinding
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

        holder.contactListItemBinding.itemFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.toggleFavorite();
                // TODO: refresh Favorites fragment. see TabsFragment.RefreshData
            }
        });

        holder.contactListItemBinding.itemBlockButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Context context = getContext();
                DialogInterface.OnClickListener onPositiveButtonClicked = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: update server that this user is blocked
                        Toast.makeText(context, "this user is blocked!!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                };
                buttonAction.blockAction(context, v, onPositiveButtonClicked);
            }
        });

        convertView.setTag(holder);

        return convertView;
    }
}

