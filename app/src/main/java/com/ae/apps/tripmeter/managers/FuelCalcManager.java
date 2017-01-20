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
package com.ae.apps.tripmeter.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ae.apps.tripmeter.models.FuelCalcResult;
import com.ae.apps.tripmeter.utils.AppConstants;

/**
 * Business logic for Fuel Calculations
 */

public class FuelCalcManager {

    private Context mContext;

    /**
     * Creates and initialises an instance of the FuelCalcManager class
     *
     * @param context
     */
    public FuelCalcManager(Context context) {
        this.mContext = context;
    }

    /**
     * Calculates the Fuel and Price needed for a trip
     *
     * @param distance
     * @param fuelPrice
     * @param mileage
     * @return
     */
    public FuelCalcResult calculateFuelAndPrice(float distance, float fuelPrice, float mileage) {
        FuelCalcResult calcResult = new FuelCalcResult();
        if (mileage > 0) {
            float fuelNeeded = distance / mileage;
            float totalFuelPrice = fuelPrice * fuelNeeded;
            calcResult.setFuelPriceTotal(totalFuelPrice);
            calcResult.setFuelQuantityNeeded(fuelNeeded);

            // Store Mileage and Current Fuel Price
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            preferences.edit().putString(AppConstants.PREF_KEY_FUEL_PRICE, String.valueOf(fuelPrice)).commit();
            preferences.edit().putString(AppConstants.PREF_KEY_MILEAGE, String.valueOf(mileage)).commit();
        } else {
            calcResult.setDataError(true);
        }
        return calcResult;
    }

}
