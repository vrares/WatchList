package com.vrares.watchlist.android.views;

public interface LoginView {
    void onSignInSuccess();

    void onSignInFailed(Exception e);

    void onGoogleLoginFailure(Exception e);

    void onFacebookLoginFailure(Exception e);
}
