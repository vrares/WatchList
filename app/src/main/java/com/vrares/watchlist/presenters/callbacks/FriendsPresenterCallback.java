package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.User;

import java.util.ArrayList;

public interface FriendsPresenterCallback {
    void onPendingAndFriendsReceived(ArrayList<User> pendingList, ArrayList<User> friendsList);
}
