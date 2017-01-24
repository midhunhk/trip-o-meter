package com.ae.apps.tripmeter.models;

import java.math.BigDecimal;

/**
 * Represents the share of a memeber
 */

public class TripMemberShare {
    private long id;
    private long tripId;
    private long memberId;
    private long expenseId;
    private BigDecimal share;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }
}
