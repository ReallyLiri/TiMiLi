package com.android.callmemaybe.UI;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.callmemaybe.UI.databinding.MyProfileBinding;

/**
 * Created by Ana on 05/02/2016.
 */
public class MyProfileFragment extends Fragment {

    public MyProfileFragment () {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MyProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.my_profile, container, false);
        return binding.getRoot();
    }
}
