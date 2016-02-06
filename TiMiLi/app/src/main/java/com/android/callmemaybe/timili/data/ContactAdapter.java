package com.android.callmemaybe.timili.data;

import android.content.Context;
import android.util.Log;
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

        //according to the examples it should be contact = getItem(position)
        //but then I get a nullPointer when I try contact.getField
        //but now the just app crashes :(
        Contact contact = contactsList[position];
        Log.d(LOG_TAG, "convertView == null:" + (convertView == null));
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.contact_list_item, parent, false);
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

        holder.userName_textview.setText(contact.getUserName());
        holder.phone_textview.setText(contact.phoneNumber);
        holder.status_textview.setText(contact.getStatus());
        holder.photoView.setImageResource(contact.photoId);
        convertView.setTag(holder);
        return convertView;
    }
}

