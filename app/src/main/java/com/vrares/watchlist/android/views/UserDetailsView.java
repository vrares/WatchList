package com.vrares.watchlist.android.views;

public interface UserDetailsView {
    void onPasswordValidationFailed(Exception exception);

    void onUserUpdated();

}
