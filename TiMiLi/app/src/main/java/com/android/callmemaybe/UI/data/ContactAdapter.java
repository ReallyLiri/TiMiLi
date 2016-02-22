package com.android.callmemaybe.UI.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.callmemaybe.UI.R;

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
        public ImageView photoView;
        public TextView userName_textview;
        public TextView phone_textview;
        public TextView status_textview;
        public TextView funnyStatus_textview;
        final Button fav_button;

        public ViewHolder(Context context, View convertView) {
            this.fav_button = (Button) convertView.findViewById(R.id.most_searched_item_status_favs_button);
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
        Log.d(LOG_TAG, "convertView == null:" + (convertView == null));
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.tabs_item, parent, false);
            holder = new ViewHolder(getContext(),  convertView);
            holder.photoView = (ImageView)
                    convertView.findViewById(R.id.most_searched_item_photo_icon);
            holder.userName_textview = (TextView)
                    convertView.findViewById(R.id.most_searched_item_username_textview);
            holder.phone_textview = (TextView)
                    convertView.findViewById(R.id.most_searched_item_phone_textview);
            holder.status_textview = (TextView)
                    convertView.findViewById(R.id.most_searched_item_status_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.d(LOG_TAG, "contact == null:" + (contact == null));
        Log.d(LOG_TAG, "username: " + (contact.getUserName()));


        //I get a nullPointer when I try contact.getField
        holder.userName_textview.setText(contact.getUserName());
        holder.phone_textview.setText(contact.getPhoneNumber());
        holder.status_textview.setText(contact.getContactStatus().userProfile.toString());
        holder.photoView.setImageURI(contact.getImageUri());
        if (contact.isFavorite()){
            holder.fav_button.setText(R.string.in_my_favs_button);
        }
        else{
            holder.fav_button.setText(R.string.not_in_my_favs_button);
        }
        holder.fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ContactAdapter", "clicked on favs button");
                if (contact.isFavorite()) {
                    Log.d("ContactAdapter", contact.getUserName() + " in favs: " + contact.isFavorite() + " should be true");
                    contact.setIsFavorite(false);
                    holder.fav_button.setText(R.string.not_in_my_favs_button);
                } else {
                    Log.d("ContactAdapter", contact.getUserName() + " in favs: " + contact.isFavorite() + " should be false");
                    contact.setIsFavorite(true);
                    holder.fav_button.setText(R.string.in_my_favs_button);
                }
            }
        });
        convertView.setTag(holder);
        return convertView;
    }
}

