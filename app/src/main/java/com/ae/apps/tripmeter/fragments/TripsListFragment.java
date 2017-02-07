package com.ae.apps.tripmeter.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ae.apps.common.managers.ContactManager;
import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.database.TripExpensesDatabase;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.managers.ExpenseContactManager;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.views.adapters.TripRecyclerViewAdapter;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A fragment representing a list of Trips. A trip can also be added.
 * <p>
 * Activities containing this fragment MUST implement the {@link ExpensesInteractionListener}
 * interface.
 */
public class TripsListFragment extends Fragment {

    private final int CONTACT_PICKER_RESULT = 1001;

    private ExpensesInteractionListener mListener;

    private TripExpensesDatabase mExpensesDatabase;

    private ExpenseContactManager mContactManager;

    private RecyclerView mRecyclerView;

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
        mExpensesDatabase = new TripExpensesDatabase(getActivity());
        mContactManager = new ExpenseContactManager(getActivity().getContentResolver());
    }

    private Trip addATrip() {

        Trip trip = new Trip();
        trip.setName("Trip ABCD " + Math.round(Math.random() * 10));
        trip.setStartDate(Calendar.getInstance().getTimeInMillis());
        trip.setMemberIds("23,45,67,88");

        mContactManager.getContactsFromIds(trip.getMemberIds());

        long result = mExpensesDatabase.createTrip(trip);
        Toast.makeText(getActivity(), "add row result " + result, Toast.LENGTH_SHORT).show();

        return trip;
    }

    private void pickContact(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICKER_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == CONTACT_PICKER_RESULT){
            // Handle Contact pick
            Uri result = data.getData();
            String contactId = result.getLastPathSegment();

            ContactVo contactVo = mContactManager.getContactInfo(contactId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips_list, container, false);

        // Set the adapter
        View recyclerView = view.findViewById(R.id.list);

        mTrips = mExpensesDatabase.getAllTrips();
        final TripRecyclerViewAdapter viewAdapter = new TripRecyclerViewAdapter(mTrips, mListener);

        if (recyclerView instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) recyclerView;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.setAdapter(viewAdapter);
        }

        // Locate the FAB and add a trip when its clicked
        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrips.add( addATrip() );
                if(null != viewAdapter) {
                    viewAdapter.notifyDataSetChanged();
                }
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

}