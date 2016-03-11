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
    public List<String> trackedUsers;

    public UserStatus(String phoneNum) {
        this.phoneNum = phoneNum;
        this.funnyStatus = "Hi! I'm using All Eyes";
        this.inactiveDays = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.trackedUsers = new ArrayList<>();
    }
}
