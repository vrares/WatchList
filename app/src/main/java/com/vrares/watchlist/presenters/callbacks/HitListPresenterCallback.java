package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.models.pojos.Movie;

import java.util.ArrayList;

public interface HitListPresenterCallback {

    void onHitListReceived(ArrayList<HitMovie> hitList);

    void onMoviesReceived(ArrayList<HitMovie> hitList);
}
