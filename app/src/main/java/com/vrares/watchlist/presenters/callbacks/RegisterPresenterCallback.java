package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.User;

/**
 * Created by rares.vultur on 11/13/2017.
 */

public interface RegisterPresenterCallback {
    void onAccountCreated(User user);

    void onAccountCreatedFailure(Exception e);

    void onUserInsertedSuccess();
}
