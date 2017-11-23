package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.Watcher;

import java.util.ArrayList;

public interface WatchersView {
    void onWatchersReceived(ArrayList<Watcher> watcherList);
}
