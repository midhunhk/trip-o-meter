package com.ae.apps.tripmeter.activities;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

/**
 * Android Unit Test
 */
@RunWith(AndroidJUnit4.class)
public class FuelCalcTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void testTotalFuelAndPrice() {
        String distance = "100";
        String fuelPrice = "70";
        String mileage = "20";

        // Type text and then press the button.
        Espresso.onView(ViewMatchers.withId(R.id.txtDistance))
                .perform(clearText(), typeText(distance), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.txtFuelPrice))
                .perform(clearText(), typeText(fuelPrice), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.txtMileage))
                .perform(clearText(), typeText(mileage), closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.btnCalculate)).perform(click());

        // Check that the text was changed.
        String expectedFuelNeeded = "Fuel Needed 5.00";
        String expectedFuelPrice = "Total Fuel Price 350.00";

        Espresso.onView(ViewMatchers.withId(R.id.lblFuelNeeded))
                .check(matches(ViewMatchers.withText(expectedFuelNeeded)));
        Espresso.onView(ViewMatchers.withId(R.id.lblTotalCost))
                .check(matches(ViewMatchers.withText(expectedFuelPrice)));
    }
}
