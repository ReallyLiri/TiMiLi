package com.android.callmemaybe.UI.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.callmemaybe.UI.databinding.TabsItemBinding;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactAdapter extends ArrayAdapter<Contact>{
    private final String LOG_TAG = ContactAdapter.class.getSimpleName();
    private Contact[] contactsList;

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public TabsItemBinding tabsItemBinding;

        public ViewHolder(TabsItemBinding tabsItemBinding) {
            this.tabsItemBinding = tabsItemBinding;
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
        if (convertView == null) {
            TabsItemBinding tabsItemBinding = TabsItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = tabsItemBinding.getRoot();

            holder = new ViewHolder(tabsItemBinding);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tabsItemBinding.setContact(contact);

        holder.tabsItemBinding.itemFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.toggleFavorite();
                // TODO: refresh Favorites fragment. see TabsFragment.RefreshData
            }
        });

        holder.tabsItemBinding.itemBlockButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogOpner();
            }

            private void dialogOpner() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to block this user?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: update server that this user is blocked
                        Toast.makeText(getContext(), "this user is blocked!!", Toast.LENGTH_LONG).show();
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

