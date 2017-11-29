package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.FavListView;
import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.presenters.callbacks.FavListPresenterCallback;

import java.util.ArrayList;

import javax.inject.Inject;

public class FavListPresenter implements FavListPresenterCallback{

    @Inject DatabaseHelper databaseHelper;

    private FavListView favListView;

    public void attach(FavListView favListView) {
        this.favListView = favListView;
    }

    public void detach() {
        this.favListView = null;
    }

    public void getFavList(ArrayList<HitMovie> favList, String uid) {
        databaseHelper.getFavList(favList, uid, this);
    }

    @Override
    public void onFavListRecieved(ArrayList<HitMovie> favList) {
        favListView.onFavListReceived(favList);
    }
}
