package com.android.callmemaybe.UI;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.callmemaybe.UI.databinding.ContactFragmentBinding;

/**
 * Created by Ana on 05/02/2016.
 */
public class ContactActivityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ContactFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.contact_fragment, container, false);
        return binding.getRoot();
    }
}
