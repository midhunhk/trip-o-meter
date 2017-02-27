package com.ae.apps.tripmeter.listeners;

/**
 * Interface that must be implemented by parent view to communicate with
 *
 * @link{ExpenseChangeListen}s about a change in expense
 */

public interface ExpenseChangeObserver {

    /**
     * Add a listener
     *
     * @param listener listener
     */
    public void addListener(ExpenseChangeListener listener);

    /**
     * Remove a listener
     *
     * @param listener listener
     */
    public void removeListener(ExpenseChangeListener listener);
}
