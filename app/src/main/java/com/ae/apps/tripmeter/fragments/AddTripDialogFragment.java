package com.ae.apps.tripmeter.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTripDialogFragment extends DialogFragment {

    /**
     *
     * @param trip
     * @return
     */
    public static AddTripDialogFragment newInstance(Trip trip) {
        AddTripDialogFragment fragment = new AddTripDialogFragment();

        Bundle argBundle = new Bundle();
        argBundle.putLong(AppConstants.KEY_TRIP_ID, trip.getId());

        fragment.setArguments(argBundle);
        // fragment.setTrip(trip);
        return fragment;
    }

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
