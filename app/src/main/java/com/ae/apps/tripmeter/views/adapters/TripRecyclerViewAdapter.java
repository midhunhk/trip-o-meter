package com.ae.apps.tripmeter.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.TRIP_DATE_FORMAT);
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

        holder.mDeleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mExpensesInteractionListener) {
                    mListUpdateListener.deleteTrip(mValues.get(position));
                }
            }
        });
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
        final ImageView mDeleteTrip;
        Trip mItem;

        ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            mView = view;
            mTripName = (TextView) view.findViewById(R.id.tripName);
            mTripDate = (TextView) view.findViewById(R.id.tripDate);
            mTripMemberCount = (TextView) view.findViewById(R.id.tripMemberCount);
            mDeleteTrip = (ImageView) view.findViewById(R.id.btnDeleteTrip);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTripDate.getText() + "'";
        }
    }
}
