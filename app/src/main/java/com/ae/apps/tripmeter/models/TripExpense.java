package com.ae.apps.tripmeter.models;

/**
 * Represents an expense
 */

public class TripExpense {
    private String id;
    private String tripId;
    private String memberIds;
    private String paidById;
    private String category;
    private String note;
    private float amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }

    public String getPaidById() {
        return paidById;
    }

    public void setPaidById(String paidById) {
        this.paidById = paidById;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
