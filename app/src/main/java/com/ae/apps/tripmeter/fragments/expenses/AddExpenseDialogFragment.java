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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.models.TripExpense;
import com.ae.apps.tripmeter.utils.AppConstants;
import com.ae.apps.tripmeter.views.adapters.ContactSpinnerAdapter;

/**
 * Add an Expense entry
 */
public class AddExpenseDialogFragment extends DialogFragment {

    private Trip trip;
    private Context mContext;
    private EditText mTxtExpenseAmount;
    private EditText mTxtExpenseNote;
    private EditText mTxtExpenseCategory;
    private LinearLayout mMembersContainer;
    private Spinner mSpinnerExpenseContributor;

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public AddExpenseDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static AddExpenseDialogFragment newInstance(Trip trip) {
        AddExpenseDialogFragment fragment = new AddExpenseDialogFragment();

        Bundle argBundle = new Bundle();
        argBundle.putString(AppConstants.KEY_TRIP_ID, trip.getId());

        fragment.setArguments(argBundle);
        fragment.setTrip(trip);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_expense_dialog, container, false);

        mContext = getContext();

        initViews(view);

        Button btnAdd = (Button) view.findViewById(R.id.btnExpenseAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactVo expenseContributor = (ContactVo) mSpinnerExpenseContributor.getSelectedItem();

                try {
                    TripExpense tripExpense = getTripExpense(expenseContributor);

                    // Pass back this TripExpense to parent fragment to store in the database
                    sendResult(tripExpense);

                    dismiss();
                } catch (ExpenseValidationException e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void initViews(View view) {
        mSpinnerExpenseContributor = (Spinner) view.findViewById(R.id.txtExpenseContributor);
        mSpinnerExpenseContributor.setAdapter(new ContactSpinnerAdapter(getActivity(), trip.getMembers()));

        mTxtExpenseAmount = (EditText) view.findViewById(R.id.txtExpenseAmount);
        mTxtExpenseNote = (EditText) view.findViewById(R.id.txtExpenseNote);
        mTxtExpenseCategory = (EditText) view.findViewById(R.id.txtExpenseCategory);
        mMembersContainer = (LinearLayout) view.findViewById(R.id.tripMembersContainer);
        addMembersToContainer();
    }

    @NonNull
    private TripExpense getTripExpense(ContactVo expenseContributor) throws ExpenseValidationException {
        TripExpense tripExpense = new TripExpense();
        if (TextUtils.isEmpty(mTxtExpenseAmount.getText())) {
            throw new ExpenseValidationException("Please Enter expense amount");
        }

        // Get selected expense members
        CheckBox checkbox;
        StringBuilder selectedMemberIds = new StringBuilder();
        for (int i = 0; i < mMembersContainer.getChildCount(); i++) {
            if (mMembersContainer.getChildAt(i) instanceof CheckBox) {
                checkbox = (CheckBox) mMembersContainer.getChildAt(i);
                if (checkbox.isChecked()) {
                    selectedMemberIds.append(checkbox.getTag()).append(",");
                }
            }
        }
        // Remove the extra "," at the end
        if (selectedMemberIds.length() > 0) {
            selectedMemberIds.deleteCharAt(selectedMemberIds.lastIndexOf(","));
        }

        if (TextUtils.isEmpty(selectedMemberIds)) {
            throw new ExpenseValidationException("Please select at least 1 member for this expense");
        }

        tripExpense.setTripId(trip.getId());
        tripExpense.setMemberIds(selectedMemberIds.toString());

        tripExpense.setAmount(Float.parseFloat(mTxtExpenseAmount.getText().toString()));
        tripExpense.setPaidById(expenseContributor.getId());
        tripExpense.setCategory(mTxtExpenseCategory.getText().toString());
        tripExpense.setNote(mTxtExpenseNote.getText().toString());

        return tripExpense;
    }

    private void addMembersToContainer() {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        CheckBox checkBox;
        for (ContactVo contactVo : trip.getMembers()) {
            checkBox = new CheckBox(getActivity());
            checkBox.setLayoutParams(layoutParams);
            checkBox.setText(contactVo.getName());
            checkBox.setTag(contactVo.getId());
            checkBox.setChecked(true);
            // Do we require an id to be supplied for each dynamically created view?
            // checkBox.setid(generateViewId());

            mMembersContainer.addView(checkBox);
        }
    }

    private void sendResult(TripExpense tripExpense) {
        if (getTargetFragment() instanceof AddExpenseDialogListener) {
            AddExpenseDialogListener expenseDialogListener = (AddExpenseDialogListener) getTargetFragment();
            expenseDialogListener.onExpenseAdded(tripExpense);
        } else {
            throw new RuntimeException("Fragment must implement AddExpenseDialogListener");
        }
    }

    /**
     *
     */
    public interface AddExpenseDialogListener {
        void onExpenseAdded(TripExpense tripExpense);
    }

    /**
     *
     */
    private class ExpenseValidationException extends Exception {

        ExpenseValidationException(String message) {
            super(message);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
