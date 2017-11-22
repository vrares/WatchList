package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.RegisterView;
import com.vrares.watchlist.android.helpers.ConnectivityHelper;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.callbacks.RegisterPresenterCallback;

import javax.inject.Inject;

public class RegisterPresenter implements RegisterPresenterCallback{

    @Inject ConnectivityHelper connectivityHelper;
    @Inject DatabaseHelper databaseHelper;

    private RegisterView registerView;

    public void attach(RegisterView registerView) {
        this.registerView = registerView;
    }

    public void detach() {
        this.registerView = null;
    }

    public void register(User user, String password) {
        connectivityHelper.register(user, password, this);
    }

    @Override
    public void onAccountCreated(User user) {
        databaseHelper.insertUserIntoDatabase(user, this, null, null);
    }

    @Override
    public void onAccountCreatedFailure(Exception e) {
        registerView.onAccountCreatedFailure(e);
    }

    @Override
    public void onUserInsertedSuccess() {
        registerView.onUserInsertedSuccess();
    }
}
