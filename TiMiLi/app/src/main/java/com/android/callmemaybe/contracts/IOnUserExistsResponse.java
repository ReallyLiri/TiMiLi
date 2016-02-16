package com.android.callmemaybe.contracts;

/**
 * Created by Liri on 16/02/2016.
 */
public interface IOnUserExistsResponse {
    void OnResponse(String userId, boolean exists);
}
