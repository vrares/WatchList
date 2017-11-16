package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.RetrofitHelper;
import com.vrares.watchlist.android.views.MovieListView;
import com.vrares.watchlist.models.pojos.PopularMovie;
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
    public void onPopularMoviesRetrieved(ArrayList<PopularMovie> popularMovieList) {
        movieListView.onPopularMoviesRetrieved(popularMovieList);
    }

    public void getMovieListByGenre(int genre, ArrayList<PopularMovie> movieList, int pageNumber) {
        retrofitHelper.getMovieListByGenre(genre, movieList, pageNumber, this);
    }
}
