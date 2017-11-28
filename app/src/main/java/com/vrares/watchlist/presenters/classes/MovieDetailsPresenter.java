package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.views.MovieDetailsView;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.presenters.callbacks.MovieDetailsPresenterCallback;

import javax.inject.Inject;

public class MovieDetailsPresenter implements MovieDetailsPresenterCallback{

    @Inject DatabaseHelper databaseHelper;
    
    private MovieDetailsView movieDetailsView;
    
    public void attach(MovieDetailsView movieDetailsView) {
        this.movieDetailsView = movieDetailsView;
    }
    
    public void detach() {
        this.movieDetailsView = null;
    }

    public void getSeenCount(Movie movie) {
        databaseHelper.getSeenCount(movie, this);
    }

    @Override
    public void onSeenCountReceived(String seenCount) {
        movieDetailsView.onSeenCountReceived(seenCount);
    }

    @Override
    public void onMovieMarkedAsFavourites() {
        movieDetailsView.onMovieMarkedAsFavourites();
    }

    @Override
    public void onFavouriteStatusReturn(boolean isFavourite) {
        movieDetailsView.onFavouriteStatusReturn(isFavourite);
    }

    @Override
    public void onMovieRemovedFromFavourites() {
        movieDetailsView.onMovieRemovedFromFavourites();
    }

    public void markAsFavourite(Movie movie) {
        databaseHelper.markAsFavourite(movie, this);
    }

    public void checkIfMovieIsFavourite(Movie movie) {
        databaseHelper.checkIfMovieIsFavourite(movie, this);
    }

    public void removeFromFavourite(Movie movie) {
        databaseHelper.removeFromFavourite(movie, this);
    }
}
