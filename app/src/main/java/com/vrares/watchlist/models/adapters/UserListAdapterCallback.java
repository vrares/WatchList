package com.vrares.watchlist.models.adapters;

import com.vrares.watchlist.models.pojos.User;

public interface UserListAdapterCallback {
    void onFriendRequestSent(User user, UserListAdapter.MyViewHolder holder, int position, User requestingUser);
}
