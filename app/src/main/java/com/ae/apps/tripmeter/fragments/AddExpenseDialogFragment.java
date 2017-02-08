package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.models.TripExpense;
import com.ae.apps.tripmeter.utils.AppConstants;
import com.ae.apps.tripmeter.views.adapters.ContactSpinnerAdapter;

/**
 */
public class AddExpenseDialogFragment extends DialogFragment {

    private Trip trip;

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

        final Spinner expenseContributor = (Spinner) view.findViewById(R.id.txtExpenseContributor);
        expenseContributor.setAdapter(new ContactSpinnerAdapter(getActivity(), trip.getMembers()));

        Button btnAdd = (Button) view.findViewById(R.id.btnExpenseAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ContactVo contactVo = (ContactVo) expenseContributor.getSelectedItem();
                TextView txtAmount = (TextView) view.findViewById(R.id.txtExpenseAmount);

                TripExpense tripExpense = new TripExpense();
                tripExpense.setAmount(Float.parseFloat(txtAmount.getText().toString()));
                tripExpense.setPaidById(contactVo.getId());
                tripExpense.setMemberIds(trip.getMemberIds());

                // Pass back this TripExpense to parent fragment to store in the database
                sendResult(tripExpense);

                dismiss();
            }
        });

        return view;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
