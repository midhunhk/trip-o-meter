/**
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ae.apps.tripmeter.database;

import android.provider.BaseColumns;

/**
 * Contains table names, columns and SQL queries for Trip O Meter Database
 *
 */
public class DatabaseConstants {

    private DatabaseConstants() {}

    // Database Config
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db_trip_expenses";

    private static final String TEXT = " TEXT";
    private static final String NUMERIC = " NUMERIC";

    // Trips Master Table
    public static final String TRIPS_MASTER_TABLE = "trips_master";
    public static final String TRIPS_MASTER_ID = BaseColumns._ID;
    public static final String TRIPS_MASTER_TRIP_NAME = "trip_name";
    public static final String TRIPS_MASTER_MEMBER_IDS = "trip_member_ids";
    public static final String TRIPS_MASTER_TRIP_START_DATE = "trip_start_date";
    public static final String TRIPS_MASTER_TRIP_TOTAL_EXPENSES = "trip_total_expenses";
    public static final String TRIPS_MASTER_EXPENSES_SETTLED = "trip_expenses_settled";

    // Trip Expenses Table
    public static final String TRIP_EXPENSE_TABLE = "trip_expense";
    public static final String TRIP_EXPENSE_ID = BaseColumns._ID;
    public static final String TRIP_EXPENSE_TRIP_ID = "expense_trip_id";
    public static final String TRIP_EXPENSE_AMOUNT = "expense_amount";
    public static final String TRIP_EXPENSE_PAID_BY = "expense_paid_by";
    public static final String TRIP_EXPENSE_MEMBERS = "expense_member_ids";
    public static final String TRIP_EXPENSE_CATEGORY = "expense_category";
    public static final String TRIP_EXPENSE_NOTE = "expense_note";

    // Trip Member Share Table
    public static final String EXPENSE_SHARE_TABLE = "expense_share";
    public static final String EXPENSE_SHARE_ID = BaseColumns._ID;
    public static final String EXPENSE_SHARE_TRIP_ID = "expense_trip_id";
    public static final String EXPENSE_SHARE_MEMBER_ID = "expense_member_id";
    public static final String EXPENSE_SHARE_EXPENSE_ID = "expense_id";
    public static final String EXPENSE_SHARE_MEMBER_SHARE = "member_share";

    public static final String TRIPS_MASTER_SQL = "CREATE TABLE " + TRIPS_MASTER_TABLE + " (" +
            TRIPS_MASTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRIPS_MASTER_TRIP_NAME + TEXT + "," +
            TRIPS_MASTER_MEMBER_IDS + TEXT + "," +
            TRIPS_MASTER_TRIP_START_DATE + NUMERIC + "," +
            TRIPS_MASTER_TRIP_TOTAL_EXPENSES + NUMERIC + "," +
            TRIPS_MASTER_EXPENSES_SETTLED +
        ")";

    public static final String [] TRIPS_MASTER_COLUMNS = {
            TRIPS_MASTER_ID,
            TRIPS_MASTER_TRIP_NAME,
            TRIPS_MASTER_MEMBER_IDS,
            TRIPS_MASTER_TRIP_START_DATE,
            TRIPS_MASTER_TRIP_TOTAL_EXPENSES,
            TRIPS_MASTER_EXPENSES_SETTLED
        };

    public static final String TRIP_EXPENSE_SQL = "CREATE TABLE " + TRIP_EXPENSE_TABLE + " (" +
            TRIP_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRIP_EXPENSE_TRIP_ID + TEXT + ", " +
            TRIP_EXPENSE_AMOUNT + NUMERIC + "," +
            TRIP_EXPENSE_PAID_BY + TEXT + "," +
            TRIP_EXPENSE_MEMBERS + TEXT + "," +
            TRIP_EXPENSE_CATEGORY + TEXT + "," +
            TRIP_EXPENSE_NOTE + TEXT +
        ")";

    public static final String [] TRIP_EXPENSE_COLUMNS = {
            TRIP_EXPENSE_ID,
            TRIP_EXPENSE_TRIP_ID,
            TRIP_EXPENSE_AMOUNT,
            TRIP_EXPENSE_PAID_BY,
            TRIP_EXPENSE_MEMBERS,
            TRIP_EXPENSE_CATEGORY,
            TRIP_EXPENSE_NOTE };

    public static final String EXPENSE_SHARE_SQL = "CREATE TABLE " + EXPENSE_SHARE_TABLE + " (" +
            EXPENSE_SHARE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EXPENSE_SHARE_TRIP_ID + TEXT + "," +
            EXPENSE_SHARE_MEMBER_ID + TEXT + "," +
            EXPENSE_SHARE_EXPENSE_ID + TEXT + "," +
            EXPENSE_SHARE_MEMBER_SHARE + NUMERIC +
         ")";

    public static final String [] EXPENSE_SHARE_COLUMNS = {
            EXPENSE_SHARE_ID,
            EXPENSE_SHARE_TRIP_ID,
            EXPENSE_SHARE_MEMBER_ID,
            EXPENSE_SHARE_EXPENSE_ID,
            EXPENSE_SHARE_MEMBER_SHARE
    };

    public static final String [] CREATE_TABLES_SQL = {TRIPS_MASTER_SQL, TRIP_EXPENSE_SQL, EXPENSE_SHARE_SQL};
}