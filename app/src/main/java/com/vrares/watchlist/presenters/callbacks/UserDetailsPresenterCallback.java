package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.User;

public interface UserDetailsPresenterCallback {

    void onUserInsertedSuccess(User user);

    void onUserUpdateFailed(Exception exception);
}
