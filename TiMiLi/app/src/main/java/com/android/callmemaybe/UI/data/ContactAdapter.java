package com.android.callmemaybe.UI.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

        convertView.setTag(holder);

        return convertView;
    }
}

