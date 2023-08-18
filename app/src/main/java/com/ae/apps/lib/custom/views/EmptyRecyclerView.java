package com.ae.apps.lib.custom.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView that supports an entry point to check if the data set is empty
 * on its lifecycle
 *
 * <pre>
 * References
 * <a href="http://stackoverflow.com/a/27801394/39540">...</a>
 * https://gist.github.com/adelnizamutdinov/31c8f054d1af4588dc5c
 * </pre>
 */
public class EmptyRecyclerView extends RecyclerView {

    private View emptyView;

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            // Decide if the EmptyView should be shown based on the item count
            final boolean showEmptyView = getAdapter().getItemCount() == 0;
            // Check if the EmptyView is currently visible
            final boolean isEmptyViewVisible = emptyView.getVisibility() == View.VISIBLE;
            // Only toggle visibility if not in correct view state
            if(showEmptyView != isEmptyViewVisible) {
                emptyView.setVisibility(showEmptyView ? View.VISIBLE : View.GONE);
                setVisibility(showEmptyView ? View.GONE : View.VISIBLE);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }
}