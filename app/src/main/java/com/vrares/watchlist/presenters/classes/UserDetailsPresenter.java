package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.views.UserDetailsView;
import com.vrares.watchlist.presenters.callbacks.UserDetailsPresenterCallback;

public class UserDetailsPresenter implements UserDetailsPresenterCallback{

    private UserDetailsView userDetailsView;

    public void attach(UserDetailsView userDetailsView) {
        this.userDetailsView = userDetailsView;
    }

    public void detach() {
        this.userDetailsView = null;
    }

}
