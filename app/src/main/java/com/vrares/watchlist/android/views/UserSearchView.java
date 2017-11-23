package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.User;

import java.util.ArrayList;

public interface UserSearchView {
    void onUserSearchComplete(ArrayList<User> usersList);
}
