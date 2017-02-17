package com.ae.apps.tripmeter.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.database.TripExpensesDatabase;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.utils.AppConstants;

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
    public static ExpenseManager newInstance(final Context context) {
        if (null == sInstance) {
            sInstance = new ExpenseManager(context);
        }
        return sInstance;
    }

    /**
     * Create an instance of ExpenseManager
     *
     * @param context new instance
     */
    private ExpenseManager(final Context context) {
        mContext = context;
        mExpensesDatabase = new TripExpensesDatabase(mContext);
        mContactManager = new ExpenseContactManager(mContext.getContentResolver());
    }
    
    //--------------------------------------------------------------------
    // Trip Management
    //--------------------------------------------------------------------

    /**
     * Adds a trip to the database
     *
     * @param trip trip to be added
     * @return trip with tripId
     */
    public Trip addTrip(final Trip trip) {
        long rowId = mExpensesDatabase.createTrip(trip);
        ContactVo defaultContact = mContactManager.getDefaultContact();
        trip.setId(rowId);
        Log.d(TAG, "Added trip with id " + rowId + " and name " + trip.getName());
        return trip;
    }

    /**
     * Delete a trip from the database
     */ 
    public void deleteTrip(final Trip trip) {
        mExpensesDatabase.removeTrip(String.valueOf(trip.getId()));
    }
    
    /**
     * Returns a trip with the tripId
     */
    public Trip getTripByTripId(String tripId){
        return mExpensesDatabase.getTrip(tripId);
    }

    /**
     * Returns all trips
     */
    public List<Trip> getAllTrips() {
        List<Trip> trips = mExpensesDatabase.getAllTrips();
        updateTripsWithContactVos(trips);
        return trips;
    }
    
    //--------------------------------------------------------------------
    // TripExpense Management
    //--------------------------------------------------------------------
    
    /**
     * Add an expense
     */
    public TripExpense addExpense(TripExpense tripExpense){
        long rowId = mExpensesDatabase.addExpense(tripExpense);
        tripExpense.setId(rowId);
        
        // Calculate share for each member
        calculateExpenseShares(tripExpense);
        
        return tripExpense;
    }
    
    /**
     * Caculate the share for each member for this expense and update the database
     */
    private void calculateExpenseShares(TripExpense tripExpense){
        List<TripMemberShare> tripMemberShares = new ArrayList<>();
        TripMemberShare tripMemberShare;
        mExpensesDatabase.addMemberShare(tripMemberShare);
    }
    
    //--------------------------------------------------------------------
    // TripExpenseShare Management
    //--------------------------------------------------------------------
    
    /**
     * Add an expense share
     */
    public TripExpense addExpenseShare(TripExpenseShare tripExpenseShare){
        long rowId = mExpensesDatabase.addExpenseShare(tripExpenseShare);
        tripExpenseShare.setId(rowId);
        return tripExpenseShare;
    }
    
    //--------------------------------------------------------------------
    // Profile Management
    //--------------------------------------------------------------------
    
    /**
     * @param contactId
     */
    public void saveDefaultProfile(String contactId) {
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(mContext);
        preferenceManager.edit().putString(AppConstants.PREF_KEY_CURRENT_PROFILE, contactId).commit();
    }

    /**
     * @return
     */
    public ContactVo getDefaultProfile() {
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(mContext);
        String profileId = preferenceManager.getString(AppConstants.PREF_KEY_CURRENT_PROFILE, "");
        if (TextUtils.isEmpty(profileId)) {
            return null;
        }
        return getContactFromContactId(profileId);
    }
    
    /**
     * Returns the default default device account.
     * You can get the name of the user from this
     *
     * Use the @link{getDefaultProfile()} method instead to get the Contact
     * @return contactVo
     */
    @Deprecated
    public ContactVo getDefaultContact() {
        return mContactManager.getDefaultContact();
    }
    
    //--------------------------------------------------------------------
    // Private methods
    //--------------------------------------------------------------------

    /**
     * Convert memberIds to List of ContactVos
     *
     * @param trips list of trips
     */
    private void updateTripsWithContactVos(final List<Trip> trips) {
        for (Trip trip : trips) {
            trip.getMembers().addAll(mContactManager.getContactsFromIds(trip.getMemberIds()));
        }
    }

    /**
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
    
    /**
     * Wraps call to ContactManager and returns list of ContactVo  from contact ids
     */
    public List<ContactVo> getContactsFromIds(String contactIds) {
        return mContactManager.getContactsFromIds(contactIds);
    }
}
