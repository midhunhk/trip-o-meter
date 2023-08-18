package com.ae.apps.tripmeter.fragments;

import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.ViewGroup;

/**
 * Base class for all Dialog Fragments used in this app
 */
public class TripMeterDialogFragment extends AppCompatDialogFragment {
    @Override
    public void onStart() {
        super.onStart();

        if(null != getDialog().getWindow()) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
