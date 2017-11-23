package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.Watcher;

import java.util.ArrayList;

public interface WatchersPresenterCallback {
    void onWatchersReceived(ArrayList<Watcher> watcherList);
}
