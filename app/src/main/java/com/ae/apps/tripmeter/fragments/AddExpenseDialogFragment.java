package com.ae.apps.tripmeter.fragments;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
    private TextView mTxtExpenseAmount;
    private LinearLayout mMembersContainer;
    private Spinner mSpinnerExpenseContributor;

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public AddExpenseDialogFragment() {
        // Required empty public constructor
    }

    public static AddExpenseDialogFragment newInstance(Trip trip) {
        AddExpenseDialogFragment fragment = new AddExpenseDialogFragment();

        Bundle argBundle = new Bundle();
        argBundle.putLong(AppConstants.KEY_TRIP_ID, trip.getId());

        fragment.setArguments(argBundle);
        fragment.setTrip(trip);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_expense_dialog, container, false);
        mContext = getContext();

        initViews(view);

        Button btnAdd = (Button) view.findViewById(R.id.btnExpenseAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                ContactVo expenseContributor = (ContactVo) mSpinnerExpenseContributor.getSelectedItem();

                try {
                    TripExpense tripExpense = getTripExpense(expenseContributor);

                    // Pass back this TripExpense to parent fragment to store in the database
                    sendResult(tripExpense);

                    dismiss();
                } catch(ExpenseValidationException e){
                    Toast.makeText(mContext, e.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void initViews(View view) {
        mSpinnerExpenseContributor = (Spinner) view.findViewById(R.id.txtExpenseContributor);
        mSpinnerExpenseContributor.setAdapter(new ContactSpinnerAdapter(getActivity(), trip.getMembers()));

        mTxtExpenseAmount = (TextView) view.findViewById(R.id.txtExpenseAmount);

        mMembersContainer = (LinearLayout) view.findViewById(R.id.tripMembersContainer);
        addMembersToContainer();
    }

    @NonNull
    private TripExpense getTripExpense(ContactVo expenseContributor) throws ExpenseValidationException{
        TripExpense tripExpense = new TripExpense();
        if(TextUtils.isEmpty(mTxtExpenseAmount.getText())){
            throw new ExpenseValidationException("enter amount");
        }
        tripExpense.setAmount(Float.parseFloat(mTxtExpenseAmount.getText().toString()));
        tripExpense.setPaidById(expenseContributor.getId());
        tripExpense.setMemberIds(trip.getMemberIds());
        return tripExpense;
    }

    private void addMembersToContainer() {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        CheckBox checkBox;
        for(ContactVo contactVo : trip.getMembers()){
            checkBox = new CheckBox(getActivity());
            checkBox.setLayoutParams(layoutParams);
            checkBox.setText(contactVo.getName());
            checkBox.setTag(contactVo.getId());

            mMembersContainer.addView(checkBox);
        }
    }

    private void sendResult(TripExpense tripExpense){
        if(getTargetFragment() instanceof AddExpenseDialogListener){
            AddExpenseDialogListener expenseDialogListener = (AddExpenseDialogListener) getTargetFragment();
            expenseDialogListener.onExpenseAdded(tripExpense);
        } else {
            throw new RuntimeException("Fragment must implement AddExpenseDialogListener");
        }
    }

    /**
     *
     */
    public interface AddExpenseDialogListener{
        void onExpenseAdded(TripExpense tripExpense);
    }

    /**
     *
     */
    private class ExpenseValidationException extends Exception{

        ExpenseValidationException(String message){
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
