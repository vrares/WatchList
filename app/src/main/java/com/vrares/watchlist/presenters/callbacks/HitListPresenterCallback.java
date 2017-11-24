package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.Movie;

import java.util.ArrayList;

public interface HitListPresenterCallback {
    void getMovieDetails(ArrayList<String> key, ArrayList<Movie> hitList);

    void onHitListReceived(ArrayList<Movie> hitList);
}
