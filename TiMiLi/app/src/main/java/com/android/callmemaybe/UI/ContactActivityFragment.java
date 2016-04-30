package com.android.callmemaybe.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.Observable;
import android.support.v4.app.Fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.callmemaybe.helpers.ButtonAction;
import com.android.callmemaybe.UI.data.Contact;
import com.android.callmemaybe.UI.databinding.ContactFragmentBinding;
import com.android.callmemaybe.helpers.ContactHelper;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactActivityFragment extends Fragment {

    private Contact mContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ContactFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.contact_fragment, container, false);

        String phoneNumber = getActivity().getIntent().getStringExtra(ContactActivity.PHONE_NUMBER_EXTRA);
        mContact = ContactHelper.getContact(phoneNumber);
        final ButtonAction buttonAction = new ButtonAction(mContact);

        if (mContact == null) {
            Toast.makeText(getContext(), "Failed to get contact for number: " + phoneNumber, Toast.LENGTH_LONG).show();
            return null;
        }

        binding.setContact(mContact);

        ImageView photo = binding.contactFragmentPhoto;
        photo.setImageBitmap(ContactHelper.photoLoader(mContact.getImageUri(), getContext()));

        ImageButton callBtn = binding.contactFragmentCallBtn;
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction.callAction(getContext());
            }
        }) ;

        ImageButton smsBtn = binding.contactFragmentSmsBtn;
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction.msgAction(getContext());
            }
        });

        ImageButton favButton = binding.contactFragmentFavButton;
        favButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = getContext();
                buttonAction.favAction(context, v);
            }
        });

        ImageButton blockBtn = binding.contactFragmentBlockButton;
        blockBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Context context = getContext();
                DialogInterface.OnClickListener onPositiveButtonClicked = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: update server that this user is blocked
                        Toast.makeText(context, "this user is blocked!!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                        //the user is blocked, so start MainActivity
                        Intent goToMainActivity = new Intent(context, MainActivity.class);
                        //the flag delets from stack's all activities comint after the target activity
                        goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(goToMainActivity);
                    }
                };
                buttonAction.blockAction(context, v, onPositiveButtonClicked);
            }
        });

        binding.activeCircle.setBackgroundResource(mContact.getActiveInPracticeDrawable());
        mContact.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == com.android.callmemaybe.UI.BR.activeInPractice) {
                    Contact senderContact = (Contact) sender;
                    if (senderContact == null || !senderContact.equals(mContact)) {
                        return;
                    }
                    binding.activeCircle.setBackgroundResource(mContact.getActiveInPracticeDrawable());
                }
            }
        });

        binding.contactFragmentTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonAction.trackAction(getContext(), mContact, binding.snackbarPosition);
            }
        });

        return binding.getRoot();
    }
}
