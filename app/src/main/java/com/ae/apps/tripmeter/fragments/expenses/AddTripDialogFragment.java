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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.managers.ExpenseManager;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

/**
 * The Dialog fragment that adds a Trip
 * <p>
 * The interface AddTripDialogListener should be implemented by invoking Activity / Fragment
 */
public class AddTripDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = "AddTripDialog";

    private final int CONTACT_PICKER_RESULT = 1001;

    private ExpenseManager mExpenseManager;

    private Collection<ContactVo> mExpenseMembers;

    private EditText txtTripName;
    private LinearLayout mMembersContainer;

    /**
     * Create a new instance of AddTripDialogFragment
     *
     * @return fragment instance
     */
    public static AddTripDialogFragment newInstance() {
        return new AddTripDialogFragment();
    }

    public AddTripDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        if(null != getDialog().getWindow()) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mExpenseManager = ExpenseManager.newInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip_dialog, container, false);

        mExpenseMembers = new HashSet<>();

        txtTripName = (EditText) view.findViewById(R.id.txtTripName);

        mMembersContainer = (LinearLayout) view.findViewById(R.id.selectedContactsContainer);

        // Set action for adding a trip
        Button btnAdd = (Button) view.findViewById(R.id.btnTripAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Trip trip = saveTrip();

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

        // Set action for add contact button
        Button btnTripMemberAdd = (Button) view.findViewById(R.id.btnTripMemberAdd);
        btnTripMemberAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });

        return view;
    }

    private Trip saveTrip() throws TripValidationException {
        if (TextUtils.isEmpty(txtTripName.getText().toString())) {
            throw new TripValidationException("Please enter trip name");
        }

        StringBuilder builder = new StringBuilder();
        for (ContactVo contactVo : mExpenseMembers) {
            builder.append(contactVo.getId()).append(AppConstants.CONTACT_ID_SEPARATOR);
        }

        if (builder.length() == 0) {
            throw new TripValidationException("Please select members for trip");
        }
        builder.deleteCharAt(builder.lastIndexOf(AppConstants.CONTACT_ID_SEPARATOR));

        Trip trip = new Trip();
        trip.setName(txtTripName.getText().toString());
        trip.setStartDate(Calendar.getInstance().getTimeInMillis());
        trip.setMemberIds(builder.toString());
        return trip;
    }

    /**
     * Send data back to parent fragment
     *
     * @param trip the newly created trip
     */
    private void sendResult(Trip trip) {
        if (getTargetFragment() instanceof AddTripDialogListener) {
            ((AddTripDialogListener) getTargetFragment()).onTripAdded(trip);
        } else {
            throw new RuntimeException(" must implement the interface AddTripDialogListener");
        }
    }

    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICKER_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == CONTACT_PICKER_RESULT) {
            // Handle Contact pick
            Uri result = data.getData();
            String contactId = result.getLastPathSegment();

            ContactVo contactVo = mExpenseManager.getContactFromContactId(contactId);

            // Debug data
            if (null != contactVo.getId()) {

                StringBuilder debugInfo = new StringBuilder("Added contact with id " + contactVo.getId());
                if (null != contactVo.getName()) {
                    debugInfo.append(" ")
                            .append(contactVo.getName());
                }

                // Toast.makeText(getActivity(), debugInfo.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, debugInfo.toString());

                // Create and add the selected contact
                addMemberToContainer(contactVo);

                mExpenseMembers.add(contactVo);
            } else {
                Log.d(TAG, "contactVo.getId() is null");
            }
        }
    }

    private void addMemberToContainer(ContactVo contactVo) {
        TextView textView = new TextView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setText(contactVo.getName());
        mMembersContainer.addView(textView);
    }

    /**
     * Activity/Fragment that invokes this DialogFragment should implement this
     * interface to pass data back
     */
    interface AddTripDialogListener {
        void onTripAdded(Trip trip);
    }

    /**
     * Exception that is thrown when validating data while creating a trip
     */
    private class TripValidationException extends Exception {

        TripValidationException(String message) {
            super(message);
        }
    }

}
