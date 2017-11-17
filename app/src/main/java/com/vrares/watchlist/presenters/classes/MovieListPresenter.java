package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.RetrofitHelper;
import com.vrares.watchlist.android.views.MovieListView;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.presenters.callbacks.MovieListPresenterCallback;

import java.util.ArrayList;

import javax.inject.Inject;

public class MovieListPresenter implements MovieListPresenterCallback {

    @Inject RetrofitHelper retrofitHelper;

    private MovieListView movieListView;

    public void attach(MovieListView movieListView) {
        this.movieListView = movieListView;
    }

    public void detach() {
        this.movieListView = null;
    }

    public void getPopularMovieList(int pageNumber) {
        retrofitHelper.getPopularMovieList(pageNumber, this);
    }

    @Override
    public void onPopularMoviesRetrieved(ArrayList<Movie> movieList) {
        movieListView.onPopularMoviesRetrieved(movieList);
    }

    @Override
    public void onGenreMoviesRetrieved(ArrayList<Movie> movieList) {
        movieListView.onGenreMoviesRetrieved(movieList);
    }

    @Override
    public void onSearchMoviesRetrieved(ArrayList<Movie> movieList) {
        movieListView.onSearchMoviesRetrieved(movieList);
    }

    public void getMovieListByGenre(int genre, int pageNumber) {
        retrofitHelper.getMovieListByGenre(genre, pageNumber, this);
    }

    public void searchMovie(String query, int pageNumber) {
        retrofitHelper.searchMovie(query, pageNumber, this);
    }
}
