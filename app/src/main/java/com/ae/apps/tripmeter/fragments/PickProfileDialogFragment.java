package com.ae.apps.tripmeter.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickProfileDialogFragment extends AppCompatDialogFragment {

    private static final int CONTACT_PICKER_RESULT = 2020;

    private String contactId;
    private FrameLayout profileContainer;

    public static PickProfileDialogFragment newInstance() {
        return new PickProfileDialogFragment();
    }

    public PickProfileDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_profile_dialog, container, false);

        Button btnPickContact = (Button) view.findViewById(R.id.btnSelectProfile);
        btnPickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });

        Button btnSelectProfile = (Button) view.findViewById(R.id.btnChooseAsProfile);
        btnSelectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(contactId);

                dismiss();
            }
        });

        profileContainer = (FrameLayout) view.findViewById(R.id.profileContainer);

        return view;
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
            contactId = result.getLastPathSegment();

            // TODO Temporary code for feedback of picking a contact
            TextView textView = new TextView(getActivity());
            textView.setText("Contact Id " + contactId);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            profileContainer.addView(textView);
        }
    }

    private void sendResult(String contactId) {
        if (getTargetFragment() instanceof SelectProfileListener) {
            ((SelectProfileListener) getTargetFragment()).onProfileSelected(contactId);
        } else {
            throw new RuntimeException(getTargetFragment() + " must implement SelectProfileListener");
        }
    }

    /**
     * Notifies the parent that a contact is chosen as the default profile
     */
    public interface SelectProfileListener {

        /**
         * @param contactId ContactVo
         */
        void onProfileSelected(String contactId);
    }

}
