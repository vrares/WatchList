package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.PopularMovie;

import java.util.ArrayList;
import java.util.List;

public interface MovieListPresenterCallback {
    void onPopularMoviesRetrieved(ArrayList<PopularMovie> popularMovieList);

    void onGenreMoviesRetrieved(ArrayList<PopularMovie> movieList);
}
