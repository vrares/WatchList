package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.PopularMovie;

import java.util.ArrayList;

public interface MovieListView {
    void onPopularMoviesRetrieved(ArrayList<PopularMovie> popularMovieList);

    void onGenreMoviesRetrieved(ArrayList<PopularMovie> movieList);
}
