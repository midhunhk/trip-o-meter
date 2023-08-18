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

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
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
