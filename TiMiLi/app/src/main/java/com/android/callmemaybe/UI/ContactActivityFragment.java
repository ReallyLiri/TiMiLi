package com.android.callmemaybe.UI;

<<<<<<< HEAD
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
=======
import android.databinding.DataBindingUtil;
>>>>>>> 3b0fe5e9a733dd4db42b946f4581b90fb0059c81
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

<<<<<<< HEAD
        View rootView = inflater.inflate(R.layout.contact_fragment, container, false);

        final String contactPhone = savedInstanceState.getString("PHONE_NUMBER");
        Contact currentContact = ContactHelper.getContact(contactPhone);

        ContactFragmentBinding binding = DataBindingUtil.setContentView(
                getActivity(), R.layout.contact_fragment);
        binding.setContact(currentContact);

        ImageView photo = (ImageView) rootView.findViewById(R.id.contact_fragment_photo);
        photo.setImageBitmap(ContactHelper.photoLoader(currentContact.getImageUri(), getContext()));

        Button callBtn = (Button) rootView.findViewById(R.id.contact_fragment_call_btn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uri = "tel:" + contactPhone;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        }) ;

        Button smsBtn = (Button) rootView.findViewById(R.id.contact_fragment_sms_btn);
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

        return rootView;
=======
        ContactFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.contact_fragment, container, false);
        return binding.getRoot();
>>>>>>> 3b0fe5e9a733dd4db42b946f4581b90fb0059c81
    }
}
