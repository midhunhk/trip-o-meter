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
package com.ae.apps.tripmeter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.ae.apps.common.db.DataBaseHelper;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.models.TripExpense;
import com.ae.apps.tripmeter.models.TripMemberShare;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides and manages the database for TripExpenses
 */
public class TripExpensesDatabase extends DataBaseHelper {

    public TripExpensesDatabase(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null,
                DatabaseConstants.DATABASE_VERSION,
                DatabaseConstants.CREATE_TABLES_SQL);
    }

    /**
     * Creates a trip and returns the created row id
     * @param trip Trip data to be added
     * @return
     */
    public long createTrip(Trip trip){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.TRIPS_MASTER_TRIP_NAME, trip.getName());
        contentValues.put(DatabaseConstants.TRIPS_MASTER_MEMBER_IDS, trip.getMemberIds());
        contentValues.put(DatabaseConstants.TRIPS_MASTER_TRIP_START_DATE, trip.getStartDate());
        contentValues.put(DatabaseConstants.TRIPS_MASTER_TRIP_TOTAL_EXPENSES, trip.getTotalExpenses());
        contentValues.put(DatabaseConstants.TRIPS_MASTER_EXPENSES_SETTLED, trip.isSettled());
        return insert(DatabaseConstants.TRIPS_MASTER_TABLE, contentValues);
    }

    /**
     * Adds a trip expense row
     * @param tripExpense expnese
     * @return
     */
    public long addExpense(TripExpense tripExpense){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.TRIP_EXPENSE_TRIP_ID, tripExpense.getTripId());
        contentValues.put(DatabaseConstants.TRIP_EXPENSE_AMOUNT, tripExpense.getAmount());
        contentValues.put(DatabaseConstants.TRIP_EXPENSE_PAID_BY, tripExpense.getPaidById());
        contentValues.put(DatabaseConstants.TRIP_EXPENSE_MEMBERS, tripExpense.getMemberIds());
        contentValues.put(DatabaseConstants.TRIP_EXPENSE_CATEGORY, tripExpense.getCategory());
        contentValues.put(DatabaseConstants.TRIP_EXPENSE_NOTE, tripExpense.getNote());
        return insert(DatabaseConstants.TRIP_EXPENSE_TABLE, contentValues);
    }

    /**
     * Adds a members share
     * @param memberShare member's share
     * @return
     */
    public long addMemberShare(TripMemberShare memberShare){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.EXPENSE_SHARE_TRIP_ID, memberShare.getTripId());
        contentValues.put(DatabaseConstants.EXPENSE_SHARE_MEMBER_ID, memberShare.getMemberId());
        contentValues.put(DatabaseConstants.EXPENSE_SHARE_EXPENSE_ID, memberShare.getExpenseId());
        contentValues.put(DatabaseConstants.EXPENSE_SHARE_MEMBER_SHARE, memberShare.getShare());
        return insert(DatabaseConstants.EXPENSE_SHARE_TABLE, contentValues);
    }
    
    /**
     * Get the total expenses for a trip
     */
    public float getTotalTripExpenses(long tripId){
        // rawQuery("SELECT SUM(" + DatabaseConstants.TRIP_EXPENSE_AMOUNT  + ") FROM " + 
        //      DatabaseConstants.TRIP_EXPENSE_TABLE + " WHERE " + DatabaseConstants.TRIP_EXPENSE_TRIP_ID + " = ?");
       return 0.0f; 
    }

    /**
     * Get all the trips
     * @return
     */
    public List<Trip> getAllTrips() {
        Cursor tripsCursor = query(DatabaseConstants.TRIPS_MASTER_TABLE,
                DatabaseConstants.TRIPS_MASTER_COLUMNS,
                null, null, null, null, null);
        List<Trip> tripsList = new ArrayList<>();
        try {
            // Create Trip models from the cursor
            while (tripsCursor.moveToNext()) {
                tripsList.add(mapTripModel(tripsCursor));
            }
        } finally{
            tripsCursor.close();
        }
        return tripsList;
    }

    /**
     * Creates a Trip model object from a database cursor
     *
     * @param cursor
     * @return
     */
    private Trip mapTripModel(Cursor cursor){
        Trip trip = new Trip();
        trip.setId(cursor.getLong(cursor.getColumnIndex(DatabaseConstants.TRIPS_MASTER_ID)));
        trip.setName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.TRIPS_MASTER_TRIP_NAME)));
        trip.setStartDate(cursor.getLong(cursor.getColumnIndex(DatabaseConstants.TRIPS_MASTER_TRIP_START_DATE)));
        trip.setMemberIds(cursor.getString(cursor.getColumnIndex(DatabaseConstants.TRIPS_MASTER_MEMBER_IDS)));
        trip.setTotalExpenses(cursor.getFloat(cursor.getColumnIndex(DatabaseConstants.TRIPS_MASTER_TRIP_TOTAL_EXPENSES)));
        return trip;
    }

    /**
     * Returns a trip model by tripId
     *
     * @param mTripId
     */
    public Trip getTrip(long mTripId) {
        String[] args = {String.valueOf(mTripId)};
        Cursor cursor = query(DatabaseConstants.TRIPS_MASTER_TABLE,
                DatabaseConstants.TRIPS_MASTER_COLUMNS,
                DatabaseConstants.TRIPS_MASTER_ID + " = ? ",
                args, null, null, null);
        if(null == cursor || cursor.getCount() == 0) {
            return null;
        }

        Trip trip;
        try {
            cursor.moveToNext();
            trip = mapTripModel(cursor);
        } finally{
            cursor.close();
        }
        return trip;
    }
}
