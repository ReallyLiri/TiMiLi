package com.android.callmemaybe.timili.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.callmemaybe.timili.R;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactAdapter extends ArrayAdapter<Contact>{

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public ImageView photoView;
        public TextView userName;
        public TextView phone_textview;
        public TextView status_textview;

        public ViewHolder() {}

    }

    public ContactAdapter(Context context, Contact[] contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position,
                   View convertView,
                   ViewGroup parent) {
        Contact contact = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.contact_list_item, parent, false);
            holder = new ViewHolder();
            holder.photoView = (ImageView) convertView.findViewById(R.id.contact_photo_icon);
            holder.userName = (TextView) convertView.findViewById(R.id.username_textview);
            holder.phone_textview = (TextView) convertView.findViewById(R.id.phone_textview);
            holder.status_textview = (TextView) convertView.findViewById(R.id.status_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.userName.setText(contact.getUserName());
        holder.phone_textview.setText(contact.phoneNumber);
        holder.status_textview.setText(contact.getStatus());
        holder.photoView.setImageResource(contact.photoId);
        convertView.setTag(holder);
        return convertView;
    }
}

