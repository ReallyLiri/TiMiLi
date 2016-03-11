package com.android.callmemaybe.contracts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liri on 05/02/2016.
 */
public class UserStatus {
    public String phoneNum;
    public String funnyStatus;
    public List<Integer> inactiveDays;
    public List<String> blockedUsers;
    public ArrayList<String> trackedUsers;

    public UserStatus(String phoneNum) {
        this.phoneNum = phoneNum;
        this.funnyStatus = "Hi! I'm using All Eyes";
        this.inactiveDays = new ArrayList<>(); //when we have no data on user then all days are active.
        this.blockedUsers = new ArrayList<>();
        this.trackedUsers = new ArrayList<>();
    }
}
