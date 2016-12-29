package com.ae.apps.tripmeter.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String FORMAT = "%.02f";
    private EditText txtTripDistance;
    private EditText txtFuelPrice;
    private EditText txtMileage;
    private TextView lblTotalCost;
    private TextView lblFuelNeeded;

    private Context mContext;

    private static String PREF_KEY_FUEL_PRICE = "pref_key_fuel_price";
    private static String PREF_KEY_MILEAGE = "pref_key_mileage";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity().getBaseContext();

        View inflatedView = inflater.inflate(R.layout.fragment_main, container, false);

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

        // Read last saved mileage and fuel price if exists
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String strFuelPrice = sharedPreferences.getString(PREF_KEY_FUEL_PRICE, null);
        String strMileage = sharedPreferences.getString(PREF_KEY_MILEAGE, null);
        if(null != strFuelPrice && null != strMileage){
            txtFuelPrice.setText(strFuelPrice);
            txtMileage.setText(strMileage);
        }

        return inflatedView;
    }

    private void validateAndCalculateTripCosts() {
        if(!TextUtils.isEmpty(txtTripDistance.getText().toString())
                && !TextUtils.isEmpty(txtFuelPrice.getText().toString())
                && !TextUtils.isEmpty(txtMileage.getText().toString())){

            // Process data and make the calculations
            float tripDistance = Float.parseFloat(txtTripDistance.getText().toString());
            float fuelPrice = Float.parseFloat(txtFuelPrice.getText().toString());
            float mileage = Float.parseFloat(txtMileage.getText().toString());

            // Check for mileage greater than 0
            if(mileage > 0){
                float fuelNeeded =  tripDistance / mileage;
                float totalFuelPrice = fuelPrice * fuelNeeded;

                // Update labels in result card view
                String strFuelNeeded = String.format(FORMAT, fuelNeeded);
                lblFuelNeeded.setText( getResources().getString(R.string.str_total_fuel_needed, strFuelNeeded));
                String strMileage = String.format(FORMAT, totalFuelPrice);
                lblTotalCost.setText(getResources().getString(R.string.str_total_fuel_price, strMileage));

                // Store Mileage and Current Fuel Price
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                preferences.edit().putString(PREF_KEY_FUEL_PRICE, strFuelNeeded).commit();
                preferences.edit().putString(PREF_KEY_MILEAGE, strMileage).commit();

            } else {
                Toast.makeText(mContext, R.string.str_error_no_mileage, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, R.string.str_error_empty_inputs, Toast.LENGTH_LONG).show();
        }
    }
}
