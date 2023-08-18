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
package com.ae.apps.tripmeter.views.adapters;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.listeners.ExpenseListUpdateListener;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Trip}
 */
public class TripRecyclerViewAdapter extends RecyclerView.Adapter<TripRecyclerViewAdapter.ViewHolder> {

    private final List<Trip> mValues;
    private final ExpensesInteractionListener mExpensesInteractionListener;
    private final ExpenseListUpdateListener mListUpdateListener;

    public TripRecyclerViewAdapter(List<Trip> items, ExpensesInteractionListener expensesInteractionListener,
                                   ExpenseListUpdateListener listUpdateListener) {
        mValues = items;
        mExpensesInteractionListener = expensesInteractionListener;
        mListUpdateListener = listUpdateListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trip, parent, false);
        view.setClickable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Trip trip = mValues.get(position);
        holder.mItem = trip;
        holder.mTripName.setText(trip.getName());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(trip.getStartDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.TRIP_DATE_FORMAT, Locale.getDefault());
        holder.mTripDate.setText(simpleDateFormat.format(calendar.getTime()));

        if (null != trip.getMemberIds() && trip.getMemberIds().trim().length() > 0) {
            int membersCount = trip.getMemberIds().split(",").length;
            holder.mTripMemberCount.setText(membersCount + " Members");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mExpensesInteractionListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mExpensesInteractionListener.showTripDetails(mValues.get(position));
                }
            }
        });

        holder.mPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.mPopupMenu, position);
            }
        });

    }

    private void showPopupMenu(View mPopupMenu, int position) {
        PopupMenu popupMenu = new PopupMenu(mPopupMenu.getContext(), mPopupMenu);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.trip_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new TripMenuItemClickListener(position));
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTripName;
        final TextView mTripDate;
        final TextView mTripMemberCount;
        final ImageView mPopupMenu;
        Trip mItem;

        ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            mView = view;
            mTripName = (TextView) view.findViewById(R.id.tripName);
            mTripDate = (TextView) view.findViewById(R.id.tripDate);
            mTripMemberCount = (TextView) view.findViewById(R.id.tripMemberCount);
            mPopupMenu = (ImageView) view.findViewById(R.id.tripMenu);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTripDate.getText() + "'";
        }
    }

    private class TripMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int mPosition;

        TripMenuItemClickListener(int position) {
            this.mPosition = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete_trip:
                    mListUpdateListener.deleteTrip(mValues.get(mPosition));
                    return true;
                case R.id.action_edit_trip_name:
                    mListUpdateListener.updateTrip(mValues.get(mPosition));
                    return true;
            }
            return false;
        }
    }


}
