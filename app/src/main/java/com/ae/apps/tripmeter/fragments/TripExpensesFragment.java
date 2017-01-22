package com.ae.apps.tripmeter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ae.apps.tripmeter.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripExpensesFragment extends Fragment {

    public TripExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment TripExpensesFragment.
     */
    public static TripExpensesFragment newInstance() {
        return new TripExpensesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_expenses, container, false);
    }

}
