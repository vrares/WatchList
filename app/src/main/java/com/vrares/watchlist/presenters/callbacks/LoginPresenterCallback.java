package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.User;

public interface LoginPresenterCallback {
    void onSignInFailed(Exception e);

    void onSignInSuccess();

    void onGoogleLoginSuccess(User user);

    void onGoogleLoginFailure(Exception e);

    void onUserInsertedSuccess();

    void onFacebookLoginFailure(Exception e);

    void onFacebookLoginSuccess(User user);
}
