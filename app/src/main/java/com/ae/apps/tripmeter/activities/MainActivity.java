/**
 * MIT License
 * <p>
 * Copyright (c) 2016 Midhun Harikumar
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ae.apps.tripmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.ae.apps.common.activities.ToolBarBaseActivity;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.fragments.FuelCalcFragment;
import com.ae.apps.tripmeter.fragments.FuelPricesFragment;
import com.ae.apps.tripmeter.fragments.TripDetailsFragment;
import com.ae.apps.tripmeter.fragments.TripsListFragment;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;

/**
 * The Main Activity
 */
public class MainActivity extends ToolBarBaseActivity
        implements ExpensesInteractionListener {

    public static final int DEFAULT_FEATURE = R.id.action_trip_calc;
    private FragmentManager mFragmentManager;
    private boolean isChildFragmentDisplayed;

    private static final int FRAGMENT_TRIP_DETAILS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set to default app theme
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();

        // Implementing BottomNavigationView
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                updateDisplayedFragment(item.getItemId(), null);
                return false;
            }
        });

        // Check for the last feature that was used by the user, else default
        int featureFragment = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getInt(AppConstants.PREF_KEY_LAST_FEATURE, DEFAULT_FEATURE);

        updateDisplayedFragment(featureFragment, null);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */
    }

    @Override
    protected int getToolbarResourceId() {
        return R.id.toolbar;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    /**
     * Update the fragment
     *
     * @param itemId id of the menu
     */
    private void updateDisplayedFragment(int itemId, Bundle bundle) {
        Fragment fragment;
        int feature = itemId;
        isChildFragmentDisplayed = false;
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch (itemId) {
            case R.id.action_trip_calc:
                fragment = FuelCalcFragment.newInstance();
                setToolbarTitle(getResources().getString(R.string.app_name));
                break;
            case R.id.action_fuel_price:
                fragment = FuelPricesFragment.newInstance();
                setToolbarTitle(getResources().getString(R.string.menu_fuel_price));
                break;
            case R.id.action_trip_expenses:
                fragment = TripsListFragment.newInstance();
                setToolbarTitle(getResources().getString(R.string.menu_trip_expenses));
                break;
            // Inner fragment of Trip Expenses
            case FRAGMENT_TRIP_DETAILS:
                // Store parent feature id
                feature = R.id.action_trip_expenses;
                fragment = TripDetailsFragment.newInstance();
                fragmentTransaction.addToBackStack("TripExpense");
                setToolbarTitle(getResources().getString(R.string.menu_trip_expenses));
                isChildFragmentDisplayed = true;
                break;
            default:
                fragment = FuelCalcFragment.newInstance();
                setToolbarTitle(getResources().getString(R.string.app_name));
        }
        // Pass in the argument bundle if it exists
        if (null != bundle ) {
            fragment.setArguments(bundle);
        }
        PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .edit().putInt(AppConstants.PREF_KEY_LAST_FEATURE, feature)
                .apply();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isChildFragmentDisplayed && mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showTripDetails(Trip trip) {
        // Pass in the trip id as a parameter when creating and switching to the details
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.KEY_TRIP_ID, trip.getId());

        updateDisplayedFragment(FRAGMENT_TRIP_DETAILS, bundle);
    }

}
