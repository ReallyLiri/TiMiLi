package com.android.callmemaybe.UI.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.Observable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.callmemaybe.UI.BR;
import com.android.callmemaybe.UI.ContactActivity;
import com.android.callmemaybe.UI.MainActivity;
import com.android.callmemaybe.UI.R;
import com.android.callmemaybe.UI.databinding.ContactListItemBinding;
import com.android.callmemaybe.contracts.ActiveInPractice;
import com.android.callmemaybe.contracts.ICloudServer;
import com.android.callmemaybe.contracts.UserStatus;
import com.android.callmemaybe.helpers.ContactHelper;
import com.android.callmemaybe.helpers.ButtonAction;
import com.android.callmemaybe.notificationService.NotificationService;
import com.android.callmemaybe.server.FireBaseCloudServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    private final String LOG_TAG = ContactAdapter.class.getSimpleName();
    private Contact[] contactsList;
    private int searchKey = 1;
    private String searchString;

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
        super(context, 0, new ArrayList<>(Arrays.asList(contacts)));
        contactsList = contacts;
        searchString = null;
    }

    public ContactAdapter(Context context, Contact[] contacts, String searchTag) {
        this(context, contacts);
        searchString = searchTag;
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

        holder.contactListItemBinding.itemFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.toggleFavorite();
                MainActivity.refreshAllData();
            }
        });

        holder.contactListItemBinding.contactTexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchString == null) {
                    ContactActivity.StartContactActivity(getContext(), phone);
                } else {
                    ContactActivity.StartContactActivity(getContext(), phone, searchString);
                }
            }
        });

        holder.contactListItemBinding.itemFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.toggleFavorite();
                MainActivity.refreshAllData();
            }
        });

        holder.contactListItemBinding.activeCircle.setBackgroundResource(contact.getActiveInPracticeDrawable());
        contact.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback()

                                             {
                                                 @Override
                                                 public void onPropertyChanged(Observable sender, int propertyId) {
                                                     if (propertyId == BR.activeInPractice) {
                                                         holder.contactListItemBinding.activeCircle.setBackgroundResource(contact.getActiveInPracticeDrawable());
                                                     }
                                                 }
                                             }

        );

        holder.contactListItemBinding.itemTrackButton.setOnClickListener(new View.OnClickListener()

                                                                         {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 Contact myContact1 = ContactHelper.getMyContact(getContext());
                                                                                 UserStatus currContactStatus = myContact1.contactStatus;
                                                                                 List<String> currTrackedList = currContactStatus.trackedUsers;
                                                                                 Log.d("Tracking", "contact = " + contact + " currTrackList = " + currTrackedList);
                                                                                 currTrackedList.add(contact.getPhoneNumber());
                                                                                 ContactHelper.updateMyContact(getContext());
                                                                                 NotificationService.NotifyOfTrackedListChanged(getContext(), ContactHelper.getMyContact(getContext()).contactStatus.trackedUsers);
                                                                             }
                                                                         }

        );

        holder.contactListItemBinding.itemBlockButton.setOnClickListener(new View.OnClickListener()

                                                                         {

                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 final Context context = getContext();
                                                                                 DialogInterface.OnClickListener onPositiveButtonClicked = new DialogInterface.OnClickListener() {
                                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                                         Contact myContact = ContactHelper.getMyContact(getContext());
                                                                                         myContact.contactStatus.blockedUsers.add(contact.getPhoneNumber());

                                                                                         ICloudServer server = new FireBaseCloudServer(context);
                                                                                         server.UpdateMyStatus(myContact.getContactStatus());

                                                                                         ContactHelper.updateMyContact(getContext());
                                                                                         MainActivity.refreshAllData();
                                                                                         Toast.makeText(getContext(), "this user is blocked!!", Toast.LENGTH_LONG).show();
                                                                                         dialog.dismiss();
                                                                                     }

                                                                                 };
                                                                                 buttonAction.blockAction(context, v, onPositiveButtonClicked);
                                                                             }
                                                                         }

        );

        convertView.setTag(holder);

        return convertView;
    }

}
