package com.vrares.watchlist.android.views;

import com.vrares.watchlist.models.pojos.Movie;

import java.util.ArrayList;

public interface MovieListView {
    void onPopularMoviesRetrieved(ArrayList<Movie> movieList);

    void onGenreMoviesRetrieved(ArrayList<Movie> movieList);

    void onSearchMoviesRetrieved(ArrayList<Movie> movieList);
}
