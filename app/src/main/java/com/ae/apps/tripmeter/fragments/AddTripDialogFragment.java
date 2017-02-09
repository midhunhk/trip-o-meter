package com.ae.apps.tripmeter.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.managers.ExpenseContactManager;
import com.ae.apps.tripmeter.models.Trip;

/**
 * The Dialog fragment that adds a Trip
 */
public class AddTripDialogFragment extends DialogFragment {

    private final int CONTACT_PICKER_RESULT = 1001;

    private ExpenseContactManager mContactManager;

    /**
     * Create a new instance of AddTripDialogFragment
     * @return fragment instance
     */
    public static AddTripDialogFragment newInstance() {
        // fragment.setTrip(trip);
        return new AddTripDialogFragment();
    }

    public AddTripDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContactManager = new ExpenseContactManager(getActivity().getContentResolver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip_dialog, container, false);

        Button btnAdd = (Button) view.findViewById(R.id.btnTripAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //do callback and save
                Trip trip = new Trip();
                trip.setName("");
                trip.setMemberIds("");

                sendResult(trip);

                dismiss();
            }
        });
        return view;
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

            ContactVo contactVo = mContactManager.getContactInfo(contactId);
            Toast.makeText(getActivity(), " id " + contactVo.getId(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     */
    public interface AddTripDialogListener{
        void onTripAdded(Trip trip);
    }

}
