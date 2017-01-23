/**
 * MIT License

 Copyright (c) 2016 Midhun Harikumar

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.managers.FuelCalcManager;
import com.ae.apps.tripmeter.models.FuelCalcResult;
import com.ae.apps.tripmeter.utils.AppConstants;

/**
 * A placeholder fragment containing a simple view.
 */
public class FuelCalcFragment extends Fragment {

    private static final String KEY_TRIP_DISTANCE = "key_trip_distance";

    private EditText txtTripDistance;
    private EditText txtFuelPrice;
    private EditText txtMileage;
    private TextView lblTotalCost;
    private TextView lblFuelNeeded;

    private Context mContext;
    private FuelCalcManager fuelCalcManager;

    public FuelCalcFragment() {
    }

    public static FuelCalcFragment newInstance(){
        return new FuelCalcFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity().getBaseContext();

        // Create an instance of the Fuel Calc Manager
        fuelCalcManager = new FuelCalcManager(mContext);

        View inflatedView = inflater.inflate(R.layout.fragment_fuel_calc, container, false);

        initializeViews(inflatedView);

        retainPreviousValues(savedInstanceState);

        return inflatedView;
    }

    private void retainPreviousValues(Bundle bundle) {
        // Read last saved mileage and fuel price if exists
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String strFuelPrice = sharedPreferences.getString(AppConstants.PREF_KEY_FUEL_PRICE, null);
        String strMileage = sharedPreferences.getString(AppConstants.PREF_KEY_MILEAGE, null);
        if(null != strFuelPrice && null != strMileage){
            txtFuelPrice.setText(strFuelPrice);
            txtMileage.setText(strMileage);
        }

        // Retain Trip Distance text on orientation changes
        if(null != bundle){
            txtTripDistance.setText(bundle.getString(KEY_TRIP_DISTANCE, ""));
        }
    }

    private void initializeViews(View inflatedView) {
        // Find text inputs and labels to show result
        txtTripDistance = (EditText) inflatedView.findViewById(R.id.txtDistance);
        txtFuelPrice = (EditText) inflatedView.findViewById(R.id.txtFuelPrice);
        txtMileage = (EditText) inflatedView.findViewById(R.id.txtMileage);
        lblFuelNeeded = (TextView) inflatedView.findViewById(R.id.lblFuelNeeded);
        lblTotalCost = (TextView) inflatedView.findViewById(R.id.lblTotalCost);

        // Show result text without placeholders on load
        lblFuelNeeded.setText( getResources().getString(R.string.str_total_fuel_needed, ""));
        lblTotalCost.setText(getResources().getString(R.string.str_total_fuel_price, ""));

        // Find and handle Calculate button clicks
        Button calculate = (Button) inflatedView.findViewById(R.id.btnCalculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndCalculateTripCosts();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(!TextUtils.isEmpty(txtTripDistance.getText().toString())){
            outState.putString(KEY_TRIP_DISTANCE, txtTripDistance.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if(null != savedInstanceState){
            txtTripDistance.setText(savedInstanceState.getString(KEY_TRIP_DISTANCE, ""));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private void validateAndCalculateTripCosts() {
        if(!TextUtils.isEmpty(txtTripDistance.getText().toString())
                && !TextUtils.isEmpty(txtFuelPrice.getText().toString())
                && !TextUtils.isEmpty(txtMileage.getText().toString())){

            // Process data and make the calculations
            float tripDistance = Float.parseFloat(txtTripDistance.getText().toString());
            float fuelPrice = Float.parseFloat(txtFuelPrice.getText().toString());
            float mileage = Float.parseFloat(txtMileage.getText().toString());

            // Calculate the fuel and price needed
            FuelCalcResult calcResult = fuelCalcManager.calculateFuelAndPrice(tripDistance, fuelPrice, mileage);

            // Check for mileage greater than 0
            if(!calcResult.isDataError()){
                // Update labels in result card view
                String strFuelNeeded = String.format(AppConstants.RESULT_FORMAT, calcResult.getFuelQuantityNeeded());
                lblFuelNeeded.setText( getResources().getString(R.string.str_total_fuel_needed, strFuelNeeded));
                String strTotalFuelPrice = String.format(AppConstants.RESULT_FORMAT, calcResult.getFuelPriceTotal());
                lblTotalCost.setText(getResources().getString(R.string.str_total_fuel_price, strTotalFuelPrice));
            } else {
                Toast.makeText(mContext, R.string.str_error_no_mileage, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, R.string.str_error_empty_inputs, Toast.LENGTH_LONG).show();
        }
    }
}
