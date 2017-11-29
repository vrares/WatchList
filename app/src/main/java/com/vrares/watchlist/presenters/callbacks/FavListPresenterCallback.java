package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.HitMovie;

import java.util.ArrayList;

public interface FavListPresenterCallback {
    void onFavListRecieved(ArrayList<HitMovie> favList);
}
