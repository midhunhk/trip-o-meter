/*
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ae.apps.tripmeter.fragments.expenses;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ae.apps.lib.custom.views.EmptyRecyclerView;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.fragments.PickProfileDialogFragment;
import com.ae.apps.tripmeter.listeners.ExpenseListUpdateListener;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.listeners.FloatingActionButtonClickListener;
import com.ae.apps.tripmeter.managers.ExpenseManager;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;
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
        implements AddTripDialogFragment.AddTripDialogListener, PickProfileDialogFragment.SelectProfileListener,
        ExpenseListUpdateListener, FloatingActionButtonClickListener {

    private static final String TAG = "TripsListFragment";

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 5005;

    private ExpensesInteractionListener mListener;

    private ExpenseManager mExpenseManager;

    private TripRecyclerViewAdapter mViewAdapter;

    private List<Trip> mTrips;

    private LayoutInflater mInflater;

    private ViewGroup mContainer;

    private View mContentView;

    private boolean createViewCompleted;

    public TripsListFragment() {
    }

    public static TripsListFragment newInstance() {
        return new TripsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;

        // Inflate a place holder view while we check for permissions required
        createNoAccessView(inflater);

        // Check if we have been granted the READ_CONTACTS Permission
        checkPermissions();

        createViewCompleted = true;

        // When rebuilding this view, we are getting the below IllegalStateException
        // The specified child already has a parent. You must call removeView() on the child's parent first
        if (null != mContentView.getParent()) {
            ((ViewGroup) mContentView.getParent())
                    .removeView(mContentView);
        }

        // If waiting for permission, mContentView will have dummy layout, else TripsListFragment Layout
        return mContentView;
    }

    private void createNoAccessView(LayoutInflater inflater) {
        mContentView = inflater.inflate(R.layout.fragment_trip_empty, mContainer, false);
        TextView noContactsAccess = (TextView) mContentView.findViewById(R.id.txtPlaceHolder);
        noContactsAccess.setText(R.string.str_permission_contacts_reason);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // API 23 (Marshmallow) and above require certain permissions to be granted explicitly by the user
            // Since we need this permission to continue, lets request the same
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CONTACTS)) {
                // Prompt the user to grant permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                        dialog.dismiss();
                    }
                })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage(R.string.str_permission_contacts_reason)
                        .setTitle(R.string.str_permission_title);
                builder.create().show();
            } else {
                // Request permission without prompting with a Rationale
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            // Permission is granted, so load the Expenses View
            createExpenseView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createExpenseView();
                } else {
                    Toast.makeText(getActivity(), R.string.str_permission_contacts_reason, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void createExpenseView() {
        View view = mInflater.inflate(R.layout.fragment_trips_list, mContainer, false);
        EmptyRecyclerView recyclerView = view.findViewById(R.id.list);
        View emptyView = view.findViewById(R.id.empty_view);

        mExpenseManager = ExpenseManager.newInstance(getActivity());
        mTrips = mExpenseManager.getAllTrips();
        mViewAdapter = new TripRecyclerViewAdapter(mTrips, mListener, this);

        if (null != recyclerView) {
            Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mViewAdapter);
            recyclerView.setEmptyView(emptyView);
        }

        // Update the main content view with the trips list layout
        mContentView = view;

        // createViewCompleted = true indicates that onCreateView method executed and this method was
        // called after onRequestPermissionsResult was completed which is an ASYNC operation
        // Hence we manually add this view as child of the container replacing any prior views
        if (createViewCompleted) {
            mContainer.removeAllViews();
            mContainer.addView(view);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExpensesInteractionListener) {
            mListener = (ExpensesInteractionListener) context;
            mListener.showAddTripFAB();
            mListener.registerFABListener(this);
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

    @Override
    public void onResume() {
        super.onResume();
        if (null != mListener) {
            mListener.showAddTripFAB();
            mListener.registerFABListener(this);
        }
    }

    private void showAddTripDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        AddTripDialogFragment dialogFragment = AddTripDialogFragment.newInstance();
        dialogFragment.setTargetFragment(TripsListFragment.this, 300);
        dialogFragment.show(fragmentManager, "fragment_add_trip");
    }

    private void showEditTripDialog(final String tripId) {
        FragmentManager fragmentManager = getFragmentManager();
        EditTripDialogFragment dialogFragment = EditTripDialogFragment.newInstance();
        dialogFragment.setTargetFragment(TripsListFragment.this, 300);

        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.KEY_TRIP_ID, tripId);
        dialogFragment.setArguments(bundle);

        dialogFragment.show(fragmentManager, "fragment_edit_trip");
    }

    @Override
    public void onTripAdded(Trip trip) {
        // add trip to database, update with the tripId
        mExpenseManager.addTrip(trip);

        Log.d(TAG, "added new row with id " + trip.getId());

        // add trip to list view
        mTrips.add(trip);
        if (null != mViewAdapter) {
            mViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTripUpdated(Trip trip) {
        mExpenseManager.updateTrip(trip);

        // Convert this trip to an index of the trips list
        int index = -1;
        for(Trip tempTrip : mTrips){
            if(TextUtils.equals(tempTrip.getId(), trip.getId())){
                index = mTrips.indexOf(tempTrip);
                break;
            }
        }

        if(index > -1){
            mTrips.get(index).setName(trip.getName());
        }

        if (null != mViewAdapter) {
            // TODO Optimize the update call?
            // mViewAdapter.notifyItemChanged(index);
            mViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onProfileSelected(String contactId) {
        // Save this id in shared preferences
        mExpenseManager.saveDefaultProfile(contactId, getContext());
    }

    @Override
    public void deleteTrip(final Trip trip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the trip from the database
                mExpenseManager.deleteTrip(trip);

                // Remove the trip from the list and refresh the list
                mTrips.remove(trip);
                if (null != mViewAdapter) {
                    mViewAdapter.notifyDataSetChanged();
                }

                Toast.makeText(getContext(), "Trip Deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle(R.string.str_trip_delete_confirm);
        builder.create().show();
    }

    @Override
    public void updateTrip(Trip trip) {
        showEditTripDialog(trip.getId());
    }

    @Override
    public void onFloatingActionClick() {
        showAddTripDialog();
    }
}
