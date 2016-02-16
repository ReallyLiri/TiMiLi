package com.android.callmemaybe.contracts;

import java.util.Set;

/**
 * Created by Liri on 05/02/2016.
 */
public interface ICloudServer {

    void DoesUserExist(String userId, IOnUserExistsResponse onUserExistsResponse);

    void DoesUsersExist(Set<String> users, IOnUsersExistResponse onUsersExistResponse);

    void UpdateMyGist(UserGist myStatus);

    boolean IsRegisteredForUserGistData(String userId);

    void RegisterForUserGistData(String userId, IOnLatestGistUpdatedListener onLatestGistUpdated);

    void UnRegisterForUserGistData(String userId);

    void UpdateMyStatus(UserStatus myStatus);

    void RegisterForUserStatusData(String userId, IOnLatestStatusUpdatedListener onLatestStatusUpdated);

    void UnRegisterForUserStatusData(String userId);

    void UnRegisterAll();
}
