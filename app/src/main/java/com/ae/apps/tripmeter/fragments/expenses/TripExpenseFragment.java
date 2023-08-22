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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ae.apps.lib.custom.views.EmptyRecyclerView;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.listeners.ExpenseChangeListener;
import com.ae.apps.tripmeter.listeners.ExpenseChangeObserver;
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
public class TripExpenseFragment extends Fragment implements ExpenseChangeListener {

    private String tripId = "";

    private ExpenseChangeObserver mObserver;
    private ExpenseManager mExpenseManager;
    private EmptyRecyclerView mRecyclerView;
    private List<TripExpense> mTripExpenses;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TripExpenseFragment() {
    }

    public static TripExpenseFragment newInstance(Bundle args, ExpenseChangeObserver observer) {
        TripExpenseFragment tripExpenseFragment = new TripExpenseFragment();
        tripExpenseFragment.mObserver = observer;
        tripExpenseFragment.setArguments(args);
        return tripExpenseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != mObserver) {
            mObserver.addListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tripexpense_list, container, false);
        EmptyRecyclerView recyclerView = view.findViewById(R.id.list);

        if (null != getArguments()) {
            tripId = getArguments().getString(AppConstants.KEY_TRIP_ID);
        }

        if (null != savedInstanceState) {
            tripId = savedInstanceState.getString(AppConstants.KEY_TRIP_ID);
        }

        // Set the adapter
        if (null != recyclerView) {
            mExpenseManager = ExpenseManager.newInstance(getContext());
            mTripExpenses = mExpenseManager.getExpensesForTrip(tripId);

            Context context = getContext();
            mRecyclerView = recyclerView;
            mRecyclerView.setAdapter(new TripExpenseRecyclerViewAdapter(mTripExpenses));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            View emptyView = view.findViewById(R.id.empty_view);
            mRecyclerView.setEmptyView(emptyView);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.KEY_TRIP_ID, tripId);
    }

    @Override
    public void onExpenseChanged() {
        mTripExpenses = mExpenseManager.getExpensesForTrip(tripId);
        mRecyclerView.setAdapter(new TripExpenseRecyclerViewAdapter(mTripExpenses));
    }
}
