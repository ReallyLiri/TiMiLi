package com.android.callmemaybe.contracts;

/**
 * Created by Liri on 05/02/2016.
 */
public enum PhysicalActivity { // see https://developers.google.com/android/reference/com/google/android/gms/location/DetectedActivity
    InVehicle,     // DetectedActivity.IN_VEHICLE,
    OnBicycle,    // DetectedActivity.ON_BICYCLE,
    OnFoot,      // DetectedActivity.ON_FOOT,
    Running,    // DetectedActivity.RUNNING,
    NotMoving, // DetectedActivity.STILL,
    Tilting,  // DetectedActivity.TILTING,
    Unknown, // DetectedActivity.UNKNOWN,
    Walking // DetectedActivity.WALKING
}