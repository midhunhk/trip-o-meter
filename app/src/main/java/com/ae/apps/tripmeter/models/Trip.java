package com.ae.apps.tripmeter.models;

import com.ae.apps.common.vo.ContactVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Trip
 */
public class Trip {
    private String id;
    private String name;
    private String memberIds;
    private long startDate;
    private float totalExpenses;
    private boolean isSettled;

    private List<ContactVo> members;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }

    public String getMemberIds() {
        return memberIds;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setTotalExpenses(float totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public float getTotalExpenses() {
        return totalExpenses;
    }

    public void setSettled(boolean settled) {
        isSettled = settled;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public List<ContactVo> getMembers() {
        if(null == members){
            members = new ArrayList<>();
        }
        return members;
    }
}
