package com.ae.apps.tripmeter.models;

import android.graphics.drawable.BitmapDrawable;

import com.ae.apps.lib.common.models.ContactInfo;

/**
 * Represents the share of a member
 */

public class TripMemberShare {
    private String id;
    private String tripId;
    private String memberId;
    private String expenseId;
    private float share;

    // Used by view
    private ContactInfo contactInfo;

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    private BitmapDrawable contactPhoto;

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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public float getShare() {
        return share;
    }

    public void setShare(float share) {
        this.share = share;
    }

    public void setContactPhoto(BitmapDrawable contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public BitmapDrawable getContactPhoto() {
        return contactPhoto;
    }
}
