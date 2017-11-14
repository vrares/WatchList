package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.ConnectivityHelper;
import com.vrares.watchlist.android.views.LoginView;
import com.vrares.watchlist.presenters.callbacks.LoginPresenterCallback;

import javax.inject.Inject;

public class LoginPresenter implements LoginPresenterCallback {

    @Inject ConnectivityHelper connectivityHelper;

    private LoginView loginView;

    public void attach(LoginView loginView) {
        this.loginView = loginView;
    }

    public void detach() {
        this.loginView = null;
    }


    public void firebaseLogin(String email, String password) {
        connectivityHelper.firebaseLogin(email, password, this);
    }

    @Override
    public void onSignInFailed(Exception e) {
        loginView.onSignInFailed(e);
    }

    @Override
    public void onSignInSuccess() {
        loginView.onSignInSuccess();
    }
}
