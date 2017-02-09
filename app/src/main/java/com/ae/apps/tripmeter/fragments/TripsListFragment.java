package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.database.TripExpensesDatabase;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.managers.ExpenseContactManager;
import com.ae.apps.tripmeter.managers.ExpenseManager;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.views.adapters.TripRecyclerViewAdapter;

import java.util.Calendar;
import java.util.List;

/**
 * A fragment representing a list of Trips. A trip can also be added.
 * <p>
 * Activities containing this fragment MUST implement the {@link ExpensesInteractionListener}
 * interface.
 */
public class TripsListFragment extends Fragment
        implements AddTripDialogFragment.AddTripDialogListener {

    private ExpensesInteractionListener mListener;

    private ExpenseManager mExpenseManager;

    private TripExpensesDatabase mExpensesDatabase;

    private ExpenseContactManager mContactManager;

    private RecyclerView mRecyclerView;

    private TripRecyclerViewAdapter mViewAdapter;

    private List<Trip> mTrips;

    public TripsListFragment() {
    }

    public static TripsListFragment newInstance() {
        TripsListFragment fragment = new TripsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExpenseManager = new ExpenseManager(getActivity());

        mExpensesDatabase = new TripExpensesDatabase(getActivity());
        mContactManager = new ExpenseContactManager(getActivity().getContentResolver());
    }

    private Trip addATrip() {

        Trip trip = new Trip();
        trip.setName("Test Trip " + Math.round(Math.random() * 10));
        trip.setStartDate(Calendar.getInstance().getTimeInMillis());
        trip.setMemberIds("27799,28104,49741");

        // Convert the memberIds to list of contact vos
        trip.getMembers().addAll(mContactManager.getContactsFromIds(trip.getMemberIds()));

        long result = mExpensesDatabase.createTrip(trip);
        Toast.makeText(getActivity(), "add row result " + result, Toast.LENGTH_SHORT).show();

        return trip;
    }

    private void updateTripsWithContactVos(){
        for (Trip trip : mTrips){
            trip.getMembers().addAll(mContactManager.getContactsFromIds(trip.getMemberIds()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips_list, container, false);
        View recyclerView = view.findViewById(R.id.list);

        mTrips = mExpensesDatabase.getAllTrips();
        updateTripsWithContactVos();
        mViewAdapter = new TripRecyclerViewAdapter(mTrips, mListener);

        if (recyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) recyclerView;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(mViewAdapter);
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

    private void showAddTripDialog(){
        FragmentManager fragmentManager = getFragmentManager();
        AddTripDialogFragment dialogFragment = AddTripDialogFragment.newInstance();
        dialogFragment.setTargetFragment(TripsListFragment.this, 300);
        // dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
        dialogFragment.show(fragmentManager, "fragment_add_trip");
    }

    @Override
    public void onTripAdded(Trip trip) {
        // add trip to database, update with the tripId
        // add trip to list view

        // mTrips.add( addATrip() );
        if(null != mViewAdapter) {
            mViewAdapter.notifyDataSetChanged();
        }
    }
}