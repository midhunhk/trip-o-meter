package com.ae.apps.tripmeter.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.managers.ExpenseManager;

/**
 * A DialogFragment that is used to select a profile from contacts list
 */
public class PickProfileDialogFragment extends AppCompatDialogFragment {

    private static final int CONTACT_PICKER_RESULT = 2020;

    private String contactId;
    private TextView mProfileName;
    private ImageView mProfileImage;
    private Bitmap mDefaultProfilePic;
    private ExpenseManager mExpenseManager;

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

        mDefaultProfilePic = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_image);

        initViews(view);

        mExpenseManager = ExpenseManager.newInstance(getActivity());

        return view;
    }

    private void initViews(View view) {
        mProfileName = (TextView) view.findViewById(R.id.profileName);
        mProfileImage = (ImageView) view.findViewById(R.id.contactImage);

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
    }

    private void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICKER_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == CONTACT_PICKER_RESULT) {

            Uri result = data.getData();
            contactId = result.getLastPathSegment();

            ContactVo profile = mExpenseManager.getContactFromContactId(contactId);
            mProfileName.setText(profile.getName());

            Drawable profileImage = new BitmapDrawable(getResources(),
                    mExpenseManager.getContactPhoto(contactId, mDefaultProfilePic));
            mProfileImage.setImageDrawable(profileImage);
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
