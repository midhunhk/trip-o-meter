package com.ae.apps.tripmeter.views.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.models.TripMemberShare;

import java.math.BigDecimal;
import java.util.List;

/**
 * Adapter for
 */
public class TripMemberShareRecyclerViewAdapter extends
        RecyclerView.Adapter<TripMemberShareRecyclerViewAdapter.ViewHolder> {

    private final List<TripMemberShare> mValues;

    public TripMemberShareRecyclerViewAdapter(List<TripMemberShare> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tripmembershare, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getContactVo().getName());
        holder.mContactImage.setImageDrawable(mValues.get(position).getContactPhoto());

        // Color code for -ve or +ve amount of share indicates amount to give or receive
        if (mValues.get(position).getShare() < 0) {
            holder.mContentView.setTextColor(Color.GREEN);
        } else if (mValues.get(position).getShare() > 0) {
            holder.mContentView.setTextColor(Color.RED);
        }
        
        BigDecimal roundedAmount = new BigDecimal(Float.toString(mValues.get(position).getShare()));
        roundedAmount = roundedAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.mContentView.setText(roundedAmount.toString());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final ImageView mContactImage;
        TripMemberShare mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.txtContactName);
            mContentView = (TextView) view.findViewById(R.id.txtExpenseAmount);
            mContactImage = (ImageView) view.findViewById(R.id.contactImage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
