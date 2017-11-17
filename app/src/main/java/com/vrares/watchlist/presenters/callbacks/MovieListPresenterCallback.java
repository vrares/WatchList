package com.vrares.watchlist.presenters.callbacks;

import com.vrares.watchlist.models.pojos.Movie;

import java.util.ArrayList;

public interface MovieListPresenterCallback {
    void onPopularMoviesRetrieved(ArrayList<Movie> movieList);

    void onGenreMoviesRetrieved(ArrayList<Movie> movieList);

    void onSearchMoviesRetrieved(ArrayList<Movie> movieList);
}
