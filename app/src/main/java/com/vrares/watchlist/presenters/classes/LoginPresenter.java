package com.vrares.watchlist.presenters.classes;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.vrares.watchlist.android.helpers.ConnectivityHelper;
import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.LoginView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.callbacks.LoginPresenterCallback;

import javax.inject.Inject;

public class LoginPresenter implements LoginPresenterCallback {

    @Inject ConnectivityHelper connectivityHelper;
    @Inject DatabaseHelper databaseHelper;

    private LoginView loginView;

    public void attach(LoginView loginView) {
        this.loginView = loginView;
    }

    public void detach() {
        this.loginView = null;
    }


    @Override
    public void onSignInFailed(Exception e) {
        loginView.onSignInFailed(e);
    }

    @Override
    public void onSignInSuccess() {
        loginView.onSignInSuccess();
    }

    @Override
    public void onGoogleLoginSuccess(User user) {
        databaseHelper.insertUserIntoDatabase(user, null, this);
    }

    @Override
    public void onGoogleLoginFailure(Exception e) {
        loginView.onGoogleLoginFailure(e);
    }

    @Override
    public void onUserInsertedSuccess() {
        loginView.onSignInSuccess();
    }

    @Override
    public void onFacebookLoginFailure(Exception e) {
        loginView.onFacebookLoginFailure(e);
    }

    @Override
    public void onFacebookLoginSuccess(User user) {
        databaseHelper.insertUserIntoDatabase(user, null, this);
    }

    @Override
    public void onUserDetailsRetrieved(User user) {
        loginView.onUserDetailsRetrieved(user);
    }

    public void firebaseLogin(String email, String password) {
        connectivityHelper.firebaseLogin(email, password, this);
    }


    public void googleLogin(GoogleSignInAccount account) {
        connectivityHelper.googleLogin(account, this);
    }

    public void facebookLogin(LoginResult loginResult) {
        connectivityHelper.facebookLogin(loginResult, this);
    }

    public void getUserDetails(String uid) {
        databaseHelper.getUserDetails(uid, this);
    }
}
