package com.ae.apps.tripmeter.fragments.expenses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.exceptions.TripValidationException;
import com.ae.apps.tripmeter.fragments.TripMeterDialogFragment;
import com.ae.apps.tripmeter.managers.ExpenseManager;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;

/**
 * Edit Trip Dialog
 */
public class EditTripDialogFragment extends TripMeterDialogFragment {

    private ExpenseManager mExpenseManager;

    private EditText txtTripName;

    private String mTripId;

    private Trip mTrip;

    public static EditTripDialogFragment newInstance() {
        return new EditTripDialogFragment();
    }

    public EditTripDialogFragment() {
        // Required empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mExpenseManager = ExpenseManager.newInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != getArguments()) {
            mTripId = getArguments().getString(AppConstants.KEY_TRIP_ID);
        }

        if (null != savedInstanceState) {
            mTripId = savedInstanceState.getString(AppConstants.KEY_TRIP_ID);
        }

        // TripId is required to be present at this point. Else we will fail at below operation
        mTrip = mExpenseManager.getTripByTripId(mTripId);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_trip_dialog, container, false);

        txtTripName = (EditText) view.findViewById(R.id.txtTripName);

        // Populate with old trip name
        txtTripName.setText(mTrip.getName());

        Button btnUpdate = (Button) view.findViewById(R.id.btnTripUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Trip trip = updateTrip();

                    sendResult(trip);

                    dismiss();
                } catch (TripValidationException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle Cancel click on the dialog
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private Trip updateTrip() throws TripValidationException{
        if (TextUtils.isEmpty(txtTripName.getText().toString())) {
            throw new TripValidationException("Please enter trip name");
        }

        mTrip.setName(txtTripName.getText().toString());

        return mTrip;
    }

    /**
     * Send data back to parent fragment
     *
     * @param trip the newly created trip
     */
    private void sendResult(Trip trip) {
        if (getTargetFragment() instanceof AddTripDialogFragment.AddTripDialogListener) {
            ((AddTripDialogFragment.AddTripDialogListener) getTargetFragment()).onTripUpdated(trip);
        } else {
            throw new RuntimeException(" must implement the interface AddTripDialogListener");
        }
    }
}
