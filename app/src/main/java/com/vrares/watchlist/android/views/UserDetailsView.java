package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.User;

public interface UserDetailsView {
    void onPasswordValidationFailed(Exception exception);

    void onUserUpdated(User user);

    void onUserUpdateFailed(Exception exception);
}
