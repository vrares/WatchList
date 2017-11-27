package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.WatchersView;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.models.pojos.Watcher;
import com.vrares.watchlist.presenters.callbacks.WatchersPresenterCallback;

import java.util.ArrayList;

import javax.inject.Inject;

public class WatchersPresenter implements WatchersPresenterCallback{

    @Inject DatabaseHelper databaseHelper;
    private WatchersView watchersView;

    public void attach(WatchersView watchersView) {
        this.watchersView = watchersView;
    }

    public void detach() {
        this.watchersView = null;
    }

    public void getWatchers(Movie movie, ArrayList<Watcher> watcherList) {
        databaseHelper.getWatchers(movie, watcherList, this);
    }

    @Override
    public void onWatchersReceived(ArrayList<Watcher> watcherList) {
        databaseHelper.getWatcherEmail(watcherList, this);
    }

    @Override
    public void onFinalWatchersReceived(ArrayList<Watcher> watcherList) {
        watchersView.onWatchersReceived(watcherList);
    }
}
