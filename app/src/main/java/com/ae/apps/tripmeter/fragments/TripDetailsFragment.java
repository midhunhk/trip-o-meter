package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.database.TripExpensesDatabase;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;

/**
 * Activities that contain this fragment must implement the
 * {@link ExpensesInteractionListener} interface
 * to handle interaction events.
 */
public class TripDetailsFragment extends Fragment {

    private long mTripId;
    private Trip mTrip;
    private TripExpensesDatabase mExpensesDatabase;
    private ExpensesInteractionListener mListener;

    public TripDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment TripDetailsFragment.
     */
    public static TripDetailsFragment newInstance() {
        return new TripDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_trip_details, container, false);

        if (null == getArguments() && null == savedInstanceState) {
            throw new IllegalArgumentException("TripId is required");
        }

        if(null != getArguments()){
            mTripId = getArguments().getLong(AppConstants.KEY_TRIP_ID);
        }

        if(null != savedInstanceState){
            mTripId = savedInstanceState.getLong(AppConstants.KEY_TRIP_ID);
        }

        mExpensesDatabase = new TripExpensesDatabase(getActivity());
        mTrip = mExpensesDatabase.getTrip(mTripId);

        TextView tripName = (TextView) inflatedView.findViewById(R.id.txtTripName);
        TextView tripDate = (TextView) inflatedView.findViewById(R.id.txtTripDate);

        tripName.setText(mTrip.getName());
        // TODO Format the date when displaying
        tripDate.setText(mTrip.getStartDate() + "");

        FloatingActionButton floatingActionButton = (FloatingActionButton) inflatedView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });

        return inflatedView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(AppConstants.KEY_TRIP_ID, mTripId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExpensesInteractionListener) {
            mListener = (ExpensesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ExpensesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     *
     * See https://guides.codepath.com/android/Using-DialogFragment#passing-data-to-parent-fragment
     */
    private void showAddExpenseDialog(){
        FragmentManager fragmentManager = getFragmentManager();
        AddExpenseDialogFragment dialogFragment = AddExpenseDialogFragment.newInstance();
        dialogFragment.setTargetFragment(TripDetailsFragment.this, 300);
        dialogFragment.show(fragmentManager, "fragment_add_expense");
    }

}
