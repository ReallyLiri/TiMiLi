package com.android.callmemaybe.UI.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        public ViewHolder() {}

    }

    public ContactAdapter(Context context, Contact[] contacts) {
        super(context, 0, contacts);
        contactsList = contacts;
    }

    @Override
    public View getView(int position,
                   View convertView,
                   ViewGroup parent) {


        Contact contact = getItem(position);
        Log.d(LOG_TAG, "convertView == null:" + (convertView == null));
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.most_searched_item, parent, false);
            holder = new ViewHolder();
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
        holder.status_textview.setText(contact.getStatus());
        holder.photoView.setImageResource(contact.getPhotoId());
        convertView.setTag(holder);
        return convertView;
    }
}

