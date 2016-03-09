package com.android.callmemaybe.UI;

import android.support.v4.app.Fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.databinding.ContactFragmentBinding;
import com.android.callmemaybe.helpers.ContactHelper;

import com.android.callmemaybe.UI.databinding.ContactFragmentBinding;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactActivityFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String contactPhone = savedInstanceState.getString("PHONE_NUMBER");
        final Contact currentContact = ContactHelper.getContact(contactPhone);

        ContactFragmentBinding binding = DataBindingUtil.setContentView(
                getActivity(), R.layout.contact_fragment);
        binding.setContact(currentContact);

        ImageView photo = binding.contactFragmentPhoto;
        photo.setImageBitmap(ContactHelper.photoLoader(currentContact.getImageUri(), getContext()));

        Button callBtn = binding.contactFragmentCallBtn;
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uri = "tel:" + contactPhone;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        }) ;

        Button smsBtn = binding.contactFragmentSmsBtn;
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + contactPhone;
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(uri));
                Intent chooser = Intent.createChooser(intent, "Send massage with...");
                startActivity(chooser);
            }
        });

        Button blockBtn = binding.contactFragmentBlockButton;
        blockBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        return binding.getRoot();
    }
}
