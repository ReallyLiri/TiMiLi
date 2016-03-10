package com.android.callmemaybe.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        ContactFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.contact_fragment, container, false);

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

        Button callBtn = binding.contactFragmentCallBtn;
        callBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonAction.callAction(getContext());

                /*String uri = "tel:" + mContact.getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);*/
            }
        }) ;

        Button smsBtn = binding.contactFragmentSmsBtn;
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction.msgAction(getContext());

                /*String uri = "tel:" + mContact.getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(uri));
                Intent chooser = Intent.createChooser(intent, "Send message with...");
                startActivity(chooser);*/
            }
        });

        Button favButton = binding.contactFragmentFavButton;
        favButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = getContext();
                buttonAction.favAction(context, v);
            }
        });

        Button blockBtn = binding.contactFragmentBlockButton;
        blockBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Context context = getContext();
                DialogInterface.OnClickListener onPositiveButtonClicked = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: update server that this user is blocked
                        Toast.makeText(context, "this user is blocked!!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                        if (getActivity().isTaskRoot()) {
                            //the app's backstack is empty, so start MainActivity
                            Intent goToMainActivity = new Intent(context, MainActivity.class);
                            //the flag delets from stack's all activities comint after the target activity
                            goToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(goToMainActivity);

                        } else {
                            //TODO: go to previous activity. we cant use finishActivity() because its a fragment
                            getActivity().finish();
                            //not sure whether to use onBackPressed, onDestroy or finish
                            //still need to delete from backstack
                        }
                    }
                };
                buttonAction.blockAction(context, v, onPositiveButtonClicked);
            }
        });

        return binding.getRoot();
    }
}
