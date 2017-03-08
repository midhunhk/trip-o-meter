package com.ae.apps.tripmeter.listeners;

import com.ae.apps.tripmeter.models.Trip;

/**
 * Listener for updates to trip list
 */

public interface ExpenseListUpdateListener {
    /**
     * Indicates the user wants to delete a trip
     *
     * @param trip Trip to be deleted
     */
    void deleteTrip(Trip trip);

    /**
     * Indicates an update to a trip
     *
     * @param trip Trip that is updated
     */
    void updateTrip(Trip trip);
}
