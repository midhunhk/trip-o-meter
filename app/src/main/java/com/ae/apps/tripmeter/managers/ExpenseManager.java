package com.ae.apps.tripmeter.managers;

import android.content.Context;
import android.util.Log;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.database.TripExpensesDatabase;
import com.ae.apps.tripmeter.models.Trip;

import java.util.Calendar;
import java.util.List;

/**
 * Manages Expenses - interacts with the database and ExpenseContactManager
 */
public class ExpenseManager {

    private final String TAG = "ExpenseManager";

    private Context mContext;

    private static ExpenseManager sInstance;

    private TripExpensesDatabase mExpensesDatabase;

    private ExpenseContactManager mContactManager;

    /**
     * Use this method to return an instance of the Expense Manager
     *
     * @param context the context
     * @return
     */
    public static ExpenseManager newInstance(final Context context){
        if(null == sInstance){
            sInstance = new ExpenseManager(context);
        }
        return sInstance;
    }

    /**
     * Create an instance of ExpenseManager
     * @param context new instance
     */
    private ExpenseManager(final Context context){
        mContext = context;
        mExpensesDatabase = new TripExpensesDatabase(mContext);
        mContactManager = new ExpenseContactManager(mContext.getContentResolver());
    }

    /**
     * Adds a trip to the database
     *
     * @param trip trip to be added
     * @return trip with tripId
     */
    public Trip addTrip(final Trip trip){
        long rowId = mExpensesDatabase.createTrip(trip);
        ContactVo defaultContact = mContactManager.getDefaultContact();
        trip.setId(rowId);
        Log.d(TAG, "Added trip with id " + rowId + " and name " + trip.getName());
        return trip;
    }

    public void deleteTrip(final Trip trip){
        mExpensesDatabase.removeTrip(String.valueOf(trip.getId()));
    }

    /**
     * Returns the default contact (You) on this device
     *
     * @return contactVo
     */
    public ContactVo getDefaultContact(){
        return mContactManager.getDefaultContact();
    }

    public List<Trip> getAllTrips(){
        List<Trip> trips = mExpensesDatabase.getAllTrips();
        updateTripsWithContactVos(trips);

        return trips;
    }

    /**
     * Convert memberIds to List of ContactVos
     *
     * @param trips list of trips
     */
    private void updateTripsWithContactVos(final List<Trip> trips){
        for (Trip trip : trips){
            trip.getMembers().addAll(mContactManager.getContactsFromIds(trip.getMemberIds()));
        }
    }

    /**
     *
     * @return
     */
    private Trip getMockTrip() {
        Trip trip = new Trip();
        trip.setName("Test Trip " + Math.round(Math.random() * 10));
        trip.setStartDate(Calendar.getInstance().getTimeInMillis());
        trip.setMemberIds("27799,28104,49741");
        return trip;
    }

    /**
     * Wraps call to ContactManager and returns a ContactVo object
     *
     * @param contactId contact Id
     * @return ContactVo object
     */
    public ContactVo getContactFromContactId(String contactId) {
        return mContactManager.getContactInfo(contactId);
    }
}
