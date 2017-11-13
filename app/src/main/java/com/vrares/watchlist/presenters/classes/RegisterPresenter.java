package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.views.RegisterView;
import com.vrares.watchlist.helpers.ConnectivityHelper;
import com.vrares.watchlist.models.User;
import com.vrares.watchlist.presenters.callbacks.RegisterPresenterCallback;

import javax.inject.Inject;

/**
 * Created by rares.vultur on 11/13/2017.
 */

public class RegisterPresenter implements RegisterPresenterCallback{

    @Inject ConnectivityHelper connectivityHelper;

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
}
