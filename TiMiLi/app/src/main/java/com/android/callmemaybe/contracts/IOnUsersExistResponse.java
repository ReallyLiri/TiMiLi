package com.android.callmemaybe.contracts;

import java.util.Map;

/**
 * Created by Liri on 16/02/2016.
 */
public interface IOnUsersExistResponse {
    void OnResponse(Map<String, Boolean> existsByUserId);
}
