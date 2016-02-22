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
    public UserProfile userProfile;
    public List<Integer> inactiveDays;
    public List<String> blockedUsers;

    public UserStatus(String phoneNum) {
        this.phoneNum = phoneNum;
        this.funnyStatus = "";
        this.userProfile = UserProfile.defaultProf;
        this.inactiveDays = new ArrayList<>();//when we have no data on user then all days are acative.
        this.blockedUsers = new ArrayList<>();
    }
}
