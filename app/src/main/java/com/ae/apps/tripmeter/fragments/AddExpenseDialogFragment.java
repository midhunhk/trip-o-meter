package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;
import com.ae.apps.tripmeter.views.adapters.ContactSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class AddExpenseDialogFragment extends DialogFragment {

    private Trip trip;

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public AddExpenseDialogFragment() {
        // Required empty public constructor
    }

    public static AddExpenseDialogFragment newInstance(Trip trip) {
        AddExpenseDialogFragment fragment = new AddExpenseDialogFragment();

        Bundle argBundle = new Bundle();
        argBundle.putLong(AppConstants.KEY_TRIP_ID, trip.getId());

        fragment.setArguments(argBundle);
        fragment.setTrip(trip);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense_dialog, container, false);

        Spinner expenseContributor = (Spinner) view.findViewById(R.id.txtExpenseContributor);
        expenseContributor.setAdapter(new ContactSpinnerAdapter(getActivity(), trip.getMembers()));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
