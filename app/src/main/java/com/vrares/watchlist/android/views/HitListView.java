package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.models.pojos.Movie;

import java.util.ArrayList;

public interface HitListView {
    void onHitListReceived(ArrayList<HitMovie> hitList);
}
