package com.vrares.watchlist.android.views;


import com.vrares.watchlist.models.pojos.User;

import java.util.ArrayList;

public interface FriendsView {
    void onPendingAndFriendsReceived(ArrayList<User> pendingList, ArrayList<User> friendsList);
}
