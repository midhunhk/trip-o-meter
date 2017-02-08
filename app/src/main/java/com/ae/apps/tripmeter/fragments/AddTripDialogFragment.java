package com.ae.apps.tripmeter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ae.apps.tripmeter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripDialogFragment extends Fragment {


    public AddTripDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_trip_dialog, container, false);
    }

}
