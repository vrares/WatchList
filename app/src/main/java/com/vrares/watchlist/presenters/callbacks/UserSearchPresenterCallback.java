package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.User;

import java.util.ArrayList;

public interface UserSearchPresenterCallback {
    void onUserSearchComplete(ArrayList<User> usersList);
}
