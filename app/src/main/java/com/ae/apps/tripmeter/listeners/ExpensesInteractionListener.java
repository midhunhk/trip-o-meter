package com.ae.apps.tripmeter.listeners;

import com.ae.apps.tripmeter.models.Trip;

/**
 * The ExpensesInteractionListener interface must be implemented by activities which host the TripExpenseFragments
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface ExpensesInteractionListener {

    /**
     * Navigate to the Trip Details
     *
     * @param trip the trip
     */
    void showTripDetails(Trip trip);

}
