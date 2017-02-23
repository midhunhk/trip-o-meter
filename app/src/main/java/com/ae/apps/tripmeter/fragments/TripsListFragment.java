/**
 * MIT License
 * Copyright (c) 2016 Midhun Harikumar
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.managers.ExpenseManager;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.views.adapters.TripRecyclerViewAdapter;

import java.util.List;

/**
 * A fragment representing a list of Trips with option to add trips
 * <p>
 * Activities containing this fragment MUST implement the {@link ExpensesInteractionListener}
 * interface.
 * </p>
 */
public class TripsListFragment extends Fragment
        implements AddTripDialogFragment.AddTripDialogListener,
        PickProfileDialogFragment.SelectProfileListener {

    private static final String TAG = "TripsListFragment";

    private ExpensesInteractionListener mListener;

    private ExpenseManager mExpenseManager;

    private TripRecyclerViewAdapter mViewAdapter;

    private List<Trip> mTrips;

    public TripsListFragment() {
    }

    public static TripsListFragment newInstance() {
        return new TripsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExpenseManager = ExpenseManager.newInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips_list, container, false);
        View list = view.findViewById(R.id.list);

        // Expenses needs default profile to be set inorder to function
        // Check if one has been selected or ask for 1 to be selected
        // Removing default profile feature
        // checkForDefaultProfile();

        mTrips = mExpenseManager.getAllTrips();
        mViewAdapter = new TripRecyclerViewAdapter(mTrips, mListener);

        if (list instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) list;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mViewAdapter);
        }

        // Locate the FAB and add a trip when its clicked
        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTripDialog();
            }
        });

        return view;
    }

    @SuppressWarnings("unused")
    private void checkForDefaultProfile() {
        // Ask ExpenseManager for the default profile
        ContactVo contactVo = mExpenseManager.getDefaultProfile();
        if (null == contactVo) {
            showSelectProfileDialog();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExpensesInteractionListener) {
            mListener = (ExpensesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showAddTripDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        AddTripDialogFragment dialogFragment = AddTripDialogFragment.newInstance();
        dialogFragment.setTargetFragment(TripsListFragment.this, 300);
        dialogFragment.show(fragmentManager, "fragment_add_trip");
    }

    private void showSelectProfileDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        PickProfileDialogFragment dialogFragment = PickProfileDialogFragment.newInstance();
        dialogFragment.setTargetFragment(TripsListFragment.this, 300);
        dialogFragment.setCancelable(false);
        dialogFragment.show(fragmentManager, "fragment_select_profile");
    }

    @Override
    public void onTripAdded(Trip trip) {
        // add trip to database, update with the tripId
        mExpenseManager.addTrip(trip);

        // Toast.makeText(getActivity(), "added new row with id " + trip.getId(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "added new row with id " + trip.getId());

        // add trip to list view
        mTrips.add(trip);
        if (null != mViewAdapter) {
            mViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onProfileSelected(String contactId) {
        // Save this id in shared preferences
        mExpenseManager.saveDefaultProfile(contactId);
    }
}
