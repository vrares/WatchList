package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.User;

/**
 * Created by rares.vultur on 11/13/2017.
 */

public interface RegisterView {
    void onAccountCreatedFailure(Exception e);

    void onUserInsertedSuccess(User user);
}
