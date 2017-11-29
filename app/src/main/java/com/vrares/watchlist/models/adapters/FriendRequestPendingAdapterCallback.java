package com.vrares.watchlist.models.adapters;

import com.vrares.watchlist.models.pojos.User;

public interface FriendRequestPendingAdapterCallback {
    void onRequestFinished(User user, int position, User receivingUser, FriendRequestPendingAdapter.MyViewHolder holder);
}
