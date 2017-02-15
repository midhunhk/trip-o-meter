package com.ae.apps.tripmeter.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 *
 * The interface AddTripDialogListener should be implemented by invoking Activity / Fragment
 */
public class AddTripDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = "AddTripDialog";

    private final int CONTACT_PICKER_RESULT = 1001;

    private ExpenseManager mExpenseManager;

    private Collection<ContactVo> mExpenseMembers;

    private EditText txtTripName;

    /**
     * Create a new instance of AddTripDialogFragment
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

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
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

        //add defaultContact(current user) to list of members
        mExpenseMembers.add(mExpenseManager.getDefaultContact());

        txtTripName = (EditText) view.findViewById(R.id.txtTripName);

        // Set action for adding a trip
        Button btnAdd = (Button) view.findViewById(R.id.btnTripAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Trip trip = saveTrip();

                sendResult(trip);

                dismiss();
            }
        });

        // Set action for add contact button
        Button btnTripMemberAdd = (Button) view.findViewById(R.id.btnTripMemberAdd);
        btnTripMemberAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });

        return view;
    }

    private Trip saveTrip() {
        Trip trip = new Trip();
        trip.setName(txtTripName.getText().toString());
        trip.setStartDate(Calendar.getInstance().getTimeInMillis());

        StringBuilder builder = new StringBuilder();
        for(ContactVo contactVo : mExpenseMembers){
            builder.append(contactVo.getId()).append(AppConstants.CONTACT_ID_SEPARATOR);
        }
        builder.deleteCharAt(builder.lastIndexOf(AppConstants.CONTACT_ID_SEPARATOR));

        trip.setMemberIds(builder.toString());
        return trip;
    }

    /**
     * Send data back to parent fragment
     * @param trip the newly created trip
     */
    private void sendResult(Trip trip) {
        if(getTargetFragment() instanceof AddTripDialogListener){
            ((AddTripDialogListener) getTargetFragment()).onTripAdded(trip);
        } else{
            throw new RuntimeException(" must implement the interface AddTripDialogListener");
        }
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

            ContactVo contactVo = mExpenseManager.getContactFromContactId(contactId);

            // Debug data
            StringBuilder debugInfo = new StringBuilder("Added contact with id " + contactVo.getId());
            if(null != contactVo.getName()){
                debugInfo.append(" " + contactVo.getName());
            }

            Toast.makeText(getActivity(), debugInfo.toString(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, debugInfo.toString());

            mExpenseMembers.add(contactVo);
        }
    }

    /**
     * Activity/Fragment that invokes this DialogFragment should implement this
     * interface to pass data back
     */
    public interface AddTripDialogListener{
        void onTripAdded(Trip trip);
    }

}
