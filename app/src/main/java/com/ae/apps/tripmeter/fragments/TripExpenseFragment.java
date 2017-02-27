package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.listeners.ExpenseChangeListener;
import com.ae.apps.tripmeter.listeners.ExpenseChangeObserver;
import com.ae.apps.tripmeter.managers.ExpenseManager;
import com.ae.apps.tripmeter.models.TripExpense;
import com.ae.apps.tripmeter.utils.AppConstants;
import com.ae.apps.tripmeter.views.adapters.TripExpenseRecyclerViewAdapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class TripExpenseFragment extends Fragment implements ExpenseChangeListener {

    private String tripId = "";

    private ExpenseChangeObserver mObserver;
    private ExpenseManager mExpenseManager;
    private RecyclerView mRecyclerView;
    private List<TripExpense> mTripExpenses;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TripExpenseFragment() {
    }

    public static TripExpenseFragment newInstance(Bundle args, ExpenseChangeObserver observer) {
        TripExpenseFragment tripExpenseFragment = new TripExpenseFragment();
        tripExpenseFragment.mObserver = observer;
        tripExpenseFragment.setArguments(args);
        return tripExpenseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != mObserver) {
            mObserver.addListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tripexpense_list, container, false);

        if (null != getArguments()) {
            tripId = getArguments().getString(AppConstants.KEY_TRIP_ID);
        }

        if (null != savedInstanceState) {
            tripId = savedInstanceState.getString(AppConstants.KEY_TRIP_ID);
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            mExpenseManager = ExpenseManager.newInstance(getContext());
            mTripExpenses = mExpenseManager.getExpensesForTrip(tripId);

            Context context = getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setAdapter(new TripExpenseRecyclerViewAdapter(mTripExpenses));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.KEY_TRIP_ID, tripId);
    }

    @Override
    public void onExpenseChanged() {
        mTripExpenses = mExpenseManager.getExpensesForTrip(tripId);
        mRecyclerView.setAdapter(new TripExpenseRecyclerViewAdapter(mTripExpenses));
    }
}
