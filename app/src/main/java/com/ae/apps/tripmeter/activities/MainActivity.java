/*
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ae.apps.tripmeter.fragments.expenses.TripsListFragment;
import com.ae.apps.tripmeter.fragments.prices.FuelPricesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.fragments.expenses.TripDetailsFragment;
import com.ae.apps.tripmeter.fragments.fuelcalc.FuelCalcFragment;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.listeners.FloatingActionButtonClickListener;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;

/**
 * The Main Activity
 */
public class MainActivity extends AppCompatActivity
        implements ExpensesInteractionListener {

    public static final int DEFAULT_FEATURE = R.id.action_trip_calc;

    private FloatingActionButtonClickListener mFloatingActionClickListener;

    private View mFloatingActionButton;
    private FragmentManager mFragmentManager;
    private boolean isChildFragmentDisplayed;

    private static final int FRAGMENT_TRIP_DETAILS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set to default app theme
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        // Implementing BottomNavigationView
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(item -> {
            updateDisplayedFragment(item.getItemId(), null);
            return true;
        });

        // Setup the Floating Action Button
        mFloatingActionButton = findViewById(R.id.fab);

        mFloatingActionButton.setOnClickListener(v -> {
            if (null != mFloatingActionClickListener) {
                mFloatingActionClickListener.onFloatingActionClick();
            } else {
                Toast.makeText(getBaseContext(),"FAB Listener is missing", Toast.LENGTH_SHORT).show();
            }
        });

        // Check for the last feature that was used by the user, else default
        int featureFragment = PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getInt(AppConstants.PREF_KEY_LAST_FEATURE, DEFAULT_FEATURE);

        updateDisplayedFragment(R.id.action_trip_calc, null);

        // Update the selected menu item in the bottom navigation view
        navigationView.setSelectedItemId(featureFragment);

        // Find the toolbar and set it as action bar
        Toolbar mToolbar = findViewById(getToolbarResourceId());
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
        }
    }

    protected int getToolbarResourceId() {
        return R.id.toolbar;
    }

    protected void setToolbarTitle(String title) {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Update the fragment
     *
     * @param itemId id of the menu
     */
    private void updateDisplayedFragment(int itemId, @Nullable Bundle bundle) {
        Fragment fragment;
        isChildFragmentDisplayed = false;

        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if(itemId == R.id.action_trip_calc) {
            fragment = FuelCalcFragment.newInstance();
            setToolbarTitle(getResources().getString(R.string.app_name));
        } else if(itemId == R.id.action_fuel_price) {
            fragment = FuelPricesFragment.newInstance();
            setToolbarTitle(getResources().getString(R.string.menu_fuel_price));
        } else if(itemId == R.id.action_trip_expenses) {
            fragment = TripsListFragment.newInstance();
            setToolbarTitle(getResources().getString(R.string.menu_trip_expenses));
        } else if(itemId == FRAGMENT_TRIP_DETAILS) {
            // Inner fragment of Trip Expenses
            // Store parent feature id
            //feature = R.id.action_trip_expenses;
            fragment = TripDetailsFragment.newInstance();
            fragmentTransaction.addToBackStack("TripExpense");
            setToolbarTitle(getResources().getString(R.string.menu_trip_expenses));
            isChildFragmentDisplayed = true;
        } else {
            fragment = FuelCalcFragment.newInstance();
            setToolbarTitle(getResources().getString(R.string.app_name));
        }

        // Pass in the argument bundle if it exists
        if (null != bundle) {
            fragment.setArguments(bundle);
        }
        PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .edit().putInt(AppConstants.PREF_KEY_LAST_FEATURE, itemId)
                .apply();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();

        // Hide the FAB only if we are not showing trip expense fragment
        if (R.id.action_trip_expenses != itemId) {
            mFloatingActionButton.setVisibility(View.INVISIBLE);
        }
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
    public void showTripDetails(@NonNull Trip trip) {
        // Pass in the trip id as a parameter when creating and switching to the details
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.KEY_TRIP_ID, trip.getId());

        updateDisplayedFragment(FRAGMENT_TRIP_DETAILS, bundle);
    }

    @Override
    public void showAddTripFAB() {
        if (null != mFloatingActionButton) {
            mFloatingActionButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void registerFABListener(FloatingActionButtonClickListener clickListener) {
        this.mFloatingActionClickListener = clickListener;
    }

}
