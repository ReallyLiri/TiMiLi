package com.android.callmemaybe.contracts;

/**
 * Created by Liri on 05/02/2016.
 */
public class UserGist {
    public String userId;
    public long timestamp;
    public int dayOfWeek; // [1,7]
    public int hourOfDay; // [0,23]
    public int minuteOfHour; // [0,59]
    public PhysicalActivity physicalActivity;
    public RingerMode ringerMode;
    public boolean isScreenOn;
}
