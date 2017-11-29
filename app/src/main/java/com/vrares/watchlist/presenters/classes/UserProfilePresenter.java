package com.vrares.watchlist.presenters.classes;

import android.widget.Button;

import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.UserProfileView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.callbacks.UserProfilePresenterCallback;

import javax.inject.Inject;
import javax.inject.Singleton;


public class UserProfilePresenter implements UserProfilePresenterCallback{

    @Inject DatabaseHelper databaseHelper;

    private UserProfileView userProfileView;

    public void attach(UserProfileView userProfileView) {
        this.userProfileView = userProfileView;
    }

    public void detach() {
        this.userProfileView = null;
    }

    public void sendFriendRequest(User user, User requestingUser) {
        databaseHelper.sendFriendRequest(user, requestingUser, this);
    }

    @Override
    public void onFriendRequestSent() {
        userProfileView.onFriendRequestSent();
    }

    public void checkFriendState(Button btnFriend, User user, User requestingUser) {
        databaseHelper.checkFriendState(btnFriend, user, requestingUser);
    }
}
