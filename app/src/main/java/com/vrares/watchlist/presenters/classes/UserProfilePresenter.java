package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.views.UserProfileView;
import com.vrares.watchlist.presenters.callbacks.UserProfilePresenterCallback;

import javax.inject.Singleton;

@Singleton
public class UserProfilePresenter implements UserProfilePresenterCallback{

    private UserProfileView userProfileView;

    public void attach(UserProfileView userProfileView) {
        this.userProfileView = userProfileView;
    }

    public void detach() {
        this.userProfileView = null;
    }

}
