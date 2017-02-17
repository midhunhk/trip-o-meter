package com.ae.apps.tripmeter.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.models.TripExpense;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TripExpense}
 */
public class TripExpenseRecyclerViewAdapter extends RecyclerView.Adapter<TripExpenseRecyclerViewAdapter.ViewHolder> {

    private final List<TripExpense> mValues;

    public TripExpenseRecyclerViewAdapter(List<TripExpense> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tripexpense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getNote());
        holder.mContentView.setText(String.valueOf(mValues.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public TripExpense mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.txtExpenseAmount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
