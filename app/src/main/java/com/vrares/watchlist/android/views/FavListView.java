package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.HitMovie;

import java.util.ArrayList;

public interface FavListView {
    void onFavListReceived(ArrayList<HitMovie> favList);
}
