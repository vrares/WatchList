package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.UserSearchView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.callbacks.UserSearchPresenterCallback;

import java.util.ArrayList;

import javax.inject.Inject;

public class UserSearchPresenter implements UserSearchPresenterCallback{

    @Inject DatabaseHelper databaseHelper;
    private UserSearchView userSearchView;

    public void attach(UserSearchView userSearchView) {
        this.userSearchView = userSearchView;
    }

    public void detach() {
        this.userSearchView = null;
    }

    public void searchForUser(String userQuery, ArrayList<User> usersList) {
        databaseHelper.searchForUser(userQuery, usersList, this);
    }

    @Override
    public void onUserSearchComplete(ArrayList<User> usersList) {
        userSearchView.onUserSearchComplete(usersList);
    }
}
