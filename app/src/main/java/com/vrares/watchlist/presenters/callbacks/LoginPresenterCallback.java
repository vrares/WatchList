package com.vrares.watchlist.presenters.callbacks;

public interface LoginPresenterCallback {
    void onSignInFailed(Exception e);

    void onSignInSuccess();
}
