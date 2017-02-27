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
import com.ae.apps.tripmeter.models.TripMemberShare;
import com.ae.apps.tripmeter.utils.AppConstants;
import com.ae.apps.tripmeter.views.adapters.TripMemberShareRecyclerViewAdapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TripMemberShareFragment extends Fragment implements ExpenseChangeListener {

    private String mTripId = "";
    private ExpenseManager mExpenseManager;
    private ExpenseChangeObserver mObserver;
    private List<TripMemberShare> mMemberShares;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TripMemberShareFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != mObserver) {
            mObserver.addListener(this);
        }
    }

    /**
     * Get a new instance of TripMemberShareFragment
     *
     * @param args initialising arguments
     * @return instance of TripMemberShareFragment
     */
    public static TripMemberShareFragment newInstance(Bundle args, ExpenseChangeObserver observer) {
        TripMemberShareFragment tripMemberShareFragment = new TripMemberShareFragment();
        tripMemberShareFragment.setArguments(args);
        tripMemberShareFragment.mObserver = observer;
        return tripMemberShareFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tripmembershare_list, container, false);

        if (null != getArguments()) {
            mTripId = getArguments().getString(AppConstants.KEY_TRIP_ID);
        }

        if (null != savedInstanceState) {
            mTripId = savedInstanceState.getString(AppConstants.KEY_TRIP_ID);
        }

        mExpenseManager = ExpenseManager.newInstance(getContext());

        // Set the adapter
        if (view instanceof RecyclerView) {
            mMemberShares = mExpenseManager.getExpenseShareForTrip(mTripId);

            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(new TripMemberShareRecyclerViewAdapter(mMemberShares));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.KEY_TRIP_ID, mTripId);
    }

    @Override
    public void onExpenseChanged() {
        mMemberShares = mExpenseManager.getExpenseShareForTrip(mTripId);
        mRecyclerView.setAdapter(new TripMemberShareRecyclerViewAdapter(mMemberShares));
    }
}
