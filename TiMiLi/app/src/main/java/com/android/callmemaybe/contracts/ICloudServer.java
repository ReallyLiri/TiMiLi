package com.android.callmemaybe.contracts;

/**
 * Created by Liri on 05/02/2016.
 */
public interface ICloudServer {

    void UpdateMyGist(UserGist myStatus);

    boolean IsRegisteredForUserGistData(String userId);

    void RegisterForUserGistData(String userId, IOnLatestGistUpdatedListener onLatestGistUpdated);

    void UnRegisterForUserGistData(String userId);

    void UpdateMyStatus(UserStatus myStatus);

    void RegisterForUserStatusData(String userId, IOnLatestStatusUpdatedListener onLatestStatusUpdated);

    void UnRegisterForUserStatusData(String userId);
}
