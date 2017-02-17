package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.managers.ExpenseManager;
import com.ae.apps.tripmeter.models.TripExpense;
import com.ae.apps.tripmeter.utils.AppConstants;
import com.ae.apps.tripmeter.views.adapters.TripExpenseRecyclerViewAdapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class TripExpenseFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TripExpenseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tripexpense_list, container, false);

        String tripId = "";
        if (null != getArguments()) {
            tripId = getArguments().getString(AppConstants.KEY_TRIP_ID);
        }

        if (null != savedInstanceState) {
            tripId = savedInstanceState.getString(AppConstants.KEY_TRIP_ID);
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            ExpenseManager expenseManager = ExpenseManager.newInstance(getContext());
            List<TripExpense> tripExpenses = expenseManager.getExpensesForTrip(tripId);

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new TripExpenseRecyclerViewAdapter(tripExpenses));
        }
        return view;
    }

}
