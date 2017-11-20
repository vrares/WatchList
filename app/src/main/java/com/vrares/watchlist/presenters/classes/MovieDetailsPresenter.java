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
}
