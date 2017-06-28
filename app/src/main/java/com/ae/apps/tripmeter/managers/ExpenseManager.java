/*
 * MIT License
 * Copyright (c) 2016 Midhun Harikumar
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ae.apps.tripmeter.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.database.TripExpensesDatabase;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.models.TripExpense;
import com.ae.apps.tripmeter.models.TripMemberShare;
import com.ae.apps.tripmeter.utils.AppConstants;

import java.util.Calendar;
import java.util.List;

/**
 * Manages Expenses - interacts with the database and ExpenseContactManager
 */
public class ExpenseManager {

    private static final String MEMBER_ID_SEPARATOR = ",";

    final String TAG = getClass().getSimpleName();

    private static ExpenseManager sInstance;

    private final Resources mResources;

    private Bitmap mDefaultProfilePic;

    private TripExpensesDatabase mExpensesDatabase;

    private ExpenseContactManager mContactManager;

    /**
     * Use this method to return an instance of the Expense Manager
     *
     * @param context the context
     * @return an instance of ExpenseManager
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
        mResources = context.getResources();
        mExpensesDatabase = new TripExpensesDatabase(context);

        mContactManager = ExpenseContactManager.newInstance(context.getContentResolver(), mResources);
        // Fetch all contacts
        // Note that permission is required in Android L and up
        mContactManager.fetchAllContacts();

        mDefaultProfilePic = BitmapFactory.decodeResource(mResources, R.drawable.default_profile_image);

        Log.d(TAG, "Created ExpenseManager instance");
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
        trip.setId(String.valueOf(rowId));
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
    public Trip getTripByTripId(String tripId) {
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
    public TripExpense addExpense(TripExpense tripExpense) {
        long rowId = mExpensesDatabase.addExpense(tripExpense);
        tripExpense.setId(String.valueOf(rowId));

        // Calculate share for each member
        calculateExpenseShares(tripExpense);

        return tripExpense;
    }

    /**
     * Gets a list of Expenses for a trip
     *
     * @param tripId tripId
     * @return list of trips
     */
    public List<TripExpense> getExpensesForTrip(String tripId) {
        return mExpensesDatabase.getExpensesForTrip(tripId);
    }

    /**
     * Get a list of Member Shares for a trip
     *
     * @param tripId tripId
     * @return List of trips
     */
    public List<TripMemberShare> getExpenseShareForTrip(String tripId) {
        List<TripMemberShare> memberShares = mExpensesDatabase.getExpenseShareForTrip(tripId);
        ContactVo contactVo;
        BitmapDrawable bitmapDrawable;
        //Bitmap defaultProfile = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_profile_image);
        for (TripMemberShare memberShare : memberShares) {
            contactVo = mContactManager.getContactInfo(memberShare.getMemberId());
            memberShare.setContactVo(contactVo);
            bitmapDrawable = new BitmapDrawable(mResources,
                    mContactManager.getContactPhoto(memberShare.getMemberId(), mDefaultProfilePic));
            memberShare.setContactPhoto(bitmapDrawable);
        }
        return memberShares;
    }

    public float getTotalTripExpenses(String tripId) {
        return mExpensesDatabase.getTotalTripExpenses(tripId);
    }

    /**
     * Calculate the share for each member for this expense and update the database
     */
    private void calculateExpenseShares(TripExpense tripExpense) {
        TripMemberShare tripMemberShare;

        float totalExpense = tripExpense.getAmount();
        String[] memberIds = tripExpense.getMemberIds().split(MEMBER_ID_SEPARATOR);
        String contributorId = tripExpense.getPaidById();
        int memberCount = memberIds.length;

        float shareAmount;
        if (memberCount > 0 && totalExpense > 0) {
            float memberShare = totalExpense / memberCount;

            for (String memberId : memberIds) {
                // Contributor's share is expense minus his share
                // -ve to indicate it is to be received
                if (memberId.equals(contributorId)) {
                    shareAmount = -(totalExpense - memberShare);
                } else {
                    shareAmount = memberShare;
                }

                // Create the MemberShare model and save in database
                tripMemberShare = new TripMemberShare();
                tripMemberShare.setTripId(tripExpense.getTripId());
                tripMemberShare.setExpenseId(tripExpense.getId());
                tripMemberShare.setMemberId(memberId);
                tripMemberShare.setShare(shareAmount);

                addExpenseShare(tripMemberShare);
            }
        }
    }

    //--------------------------------------------------------------------
    // TripExpenseShare Management
    //--------------------------------------------------------------------

    /**
     * Add an expense share
     *
     * @param tripExpenseShare share
     * @return share with updated id
     */
    public TripMemberShare addExpenseShare(TripMemberShare tripExpenseShare) {
        long rowId = mExpensesDatabase.addMemberShare(tripExpenseShare);
        tripExpenseShare.setId(String.valueOf(rowId));
        return tripExpenseShare;
    }

    //--------------------------------------------------------------------
    // Profile Management
    //--------------------------------------------------------------------

    /**
     * @param contactId the contactId
     */
    public void saveDefaultProfile(String contactId, Context context) {
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(context);
        preferenceManager.edit().putString(AppConstants.PREF_KEY_CURRENT_PROFILE, contactId).apply();
    }

    /**
     * @return ContactVo
     */
    public ContactVo getDefaultProfile(Context context) {
        SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(context);
        String profileId = preferenceManager.getString(AppConstants.PREF_KEY_CURRENT_PROFILE, "");
        if (TextUtils.isEmpty(profileId)) {
            return null;
        }
        return getContactFromContactId(profileId);
    }

    /**
     * Returns the default default device account.
     * You can get the name of the user from this
     * <p>
     * Use the @link{getDefaultProfile()} method instead to get the Contact
     *
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

    /**
     * Wraps call to ContactManager to read the contact's image
     *
     * @param contactId    the contactId
     * @param defaultImage a default contact image
     * @return
     */
    public Bitmap getContactPhoto(final String contactId, final Bitmap defaultImage) {
        return mContactManager.getContactPhoto(contactId, defaultImage);
    }

    /**
     * Gets the contact photo, gives a default if none exists
     *
     * @param contactId
     * @return
     */
    public Bitmap getContactPhoto(final String contactId) {
        return mContactManager.getContactPhoto(contactId, mDefaultProfilePic);
    }
}
