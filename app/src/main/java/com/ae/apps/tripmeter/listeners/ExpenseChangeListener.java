package com.ae.apps.tripmeter.listeners;

/**
 * Interface that must be implemented to listen to Expense Changes
 */

public interface ExpenseChangeListener {

    /**
     * Invioked when an expense is added or removed
     */
    public void onExpenseChanged();

}
