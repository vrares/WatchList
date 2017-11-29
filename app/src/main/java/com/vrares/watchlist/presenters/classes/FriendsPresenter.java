package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.FriendsView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.callbacks.FriendsPresenterCallback;

import java.util.ArrayList;

import javax.inject.Inject;

public class FriendsPresenter implements FriendsPresenterCallback{

    @Inject DatabaseHelper databaseHelper;

    private FriendsView friendsView;

    public void attach(FriendsView friendsView) {
        this.friendsView = friendsView;
    }

    public void detach() {
        this.friendsView = null;
    }

    public void getPendingAndFriendsList(ArrayList<User> pendingList, ArrayList<User> friendsList, String uId) {
        databaseHelper.getPendingAndFriendsList(pendingList, friendsList, uId, this);
    }

    @Override
    public void onPendingAndFriendsReceived(ArrayList<User> pendingList, ArrayList<User> friendsList) {
        if (friendsView != null) {
            friendsView.onPendingAndFriendsReceived(pendingList, friendsList);
        }
    }
}
