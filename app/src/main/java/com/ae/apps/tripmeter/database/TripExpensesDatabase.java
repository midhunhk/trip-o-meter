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
        return 0;
    }

    /**
     * Adds a members share
     * @param memberShare member's share
     * @return
     */
    public long addMemberShare(TripMemberShare memberShare){
        return 0;
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
            Trip trip;
            while (tripsCursor.moveToNext()) {
                trip = new Trip();
                trip.setId(tripsCursor.getLong(tripsCursor.getColumnIndex(DatabaseConstants.TRIPS_MASTER_ID)));
                trip.setName(tripsCursor.getString(tripsCursor.getColumnIndex(DatabaseConstants.TRIPS_MASTER_TRIP_NAME)));
                trip.setStartDate(tripsCursor.getInt(tripsCursor.getColumnIndex(DatabaseConstants.TRIPS_MASTER_TRIP_START_DATE)));
                tripsList.add(trip);
            }
        } finally{
            tripsCursor.close();
        }
        return tripsList;
    }
}
