package com.ae.apps.tripmeter.views.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.fragments.TripDetailsFragment;
import com.ae.apps.tripmeter.fragments.TripExpenseFragment;
import com.ae.apps.tripmeter.utils.AppConstants;

/**
 * Created by user on 2/18/2017.
 */

public class ExpensesPagerAdapter extends FragmentPagerAdapter {

    private Bundle mFragmentArgs;
    private Context mContext;
    private int tabTitles[] = {R.string.str_expense_category, R.string.str_trip_distance};

    public ExpensesPagerAdapter(FragmentManager fm, Context context, Bundle args) {
        super(fm);
        mContext = context;
        mFragmentArgs = args;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TripExpenseFragment();
                break;
            case 1:
                fragment = new TripExpenseFragment();
                break;
        }

        // Pass the tripId to the fragments
        if (null != fragment) {
            fragment.setArguments(mFragmentArgs);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(tabTitles[position]);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
