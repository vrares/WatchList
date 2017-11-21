package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.User;

public interface LoginView {
    void onSignInSuccess();

    void onSignInFailed(Exception e);

    void onGoogleLoginFailure(Exception e);

    void onFacebookLoginFailure(Exception e);

    void onUserDetailsRetrieved(User user);
}
