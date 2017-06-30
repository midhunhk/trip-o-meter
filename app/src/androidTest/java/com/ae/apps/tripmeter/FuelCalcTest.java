package com.ae.apps.tripmeter.activities;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class FuelCalcTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void testTotalFuelAndPrice() {
		String distance = "100";
		String fuelPrice = "70";
		String mileage = "20";
		
        // Type text and then press the button.
        onView(withId(R.id.txtDistance))
                .perform(typeText(distance), closeSoftKeyboard());
		    onView(withId(R.id.txtFuelPrice))
                .perform(typeText(fuelPrice), closeSoftKeyboard());
		    onView(withId(R.id.txtMileage))
                .perform(typeText(mileage), closeSoftKeyboard());
				
        onView(withId(R.id.btnCalculate)).perform(click());

        // Check that the text was changed.
		    String expectedFuelNeeded = "Fuel Needed 5.00";
		    String expectedFuelPrice = "Total Fuel Price 350.00";
		
        onView(withId(R.id.lblFuelNeeded))
                .check(matches(withText(expectedFuelNeeded)));
		    onView(withId(R.id.lblTotalCost))
                .check(matches(withText(expectedFuelPrice)));
    }
}
