package com.ae.apps.tripmeter.managers;

import android.content.Context;

import com.ae.apps.tripmeter.database.TripExpensesDatabase;

/**
 * Manages Expenses - interacts with the database and ExpenseContactManager
 */
public class ExpenseManager {

    private Context mContext;

    private TripExpensesDatabase mExpensesDatabase;

    private ExpenseContactManager mContactManager;

    /**
     * Create an instance of ExpenseManager
     * @param context new instance
     */
    public ExpenseManager(Context context){
        mContext = context;
        mExpensesDatabase = new TripExpensesDatabase(mContext);
        mContactManager = new ExpenseContactManager(mContext.getContentResolver());
    }

}
