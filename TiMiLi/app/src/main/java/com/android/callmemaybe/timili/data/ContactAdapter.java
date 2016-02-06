package com.android.callmemaybe.timili.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.callmemaybe.timili.R;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactAdapter extends CursorAdapter{

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView photoView;
        public final TextView userName;
        public final TextView phone_textview;
        public final TextView status_textview;


        public ViewHolder(View view) {
            photoView = (ImageView) view.findViewById(R.id.contact_photo_icon);
            userName = (TextView) view.findViewById(R.id.username_textview);
            phone_textview = (TextView) view.findViewById(R.id.phone_textview);
            status_textview = (TextView) view.findViewById(R.id.status_textview);

        }
    }

    public ContactAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    //I have no idea what the function is supposed to do
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = -1;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        //needs to get data from cursor and update the views
    }
}
