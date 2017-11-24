package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.ConnectivityHelper;
import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.UserDetailsView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.callbacks.UserDetailsPresenterCallback;

import javax.inject.Inject;

public class UserDetailsPresenter implements UserDetailsPresenterCallback{

    @Inject ConnectivityHelper connectivityHelper;
    @Inject DatabaseHelper databaseHelper;
    private UserDetailsView userDetailsView;

    public void attach(UserDetailsView userDetailsView) {
        this.userDetailsView = userDetailsView;
    }

    public void detach() {
        this.userDetailsView = null;
    }

    public void updateData(User newUser) {
        databaseHelper.insertUserIntoDatabase(newUser, null, null, this);

    }

    @Override
    public void onPasswordValidationFailed(Exception exception) {
        userDetailsView.onPasswordValidationFailed(exception);
    }

    @Override
    public void onPasswordValidated(User newUser) {
    }

    @Override
    public void onUserInsertedSuccess(User user) {
        userDetailsView.onUserUpdated(user);
    }

    @Override
    public void onUserUpdateFailed(Exception exception) {
        userDetailsView.onUserUpdateFailed(exception);
    }
}
