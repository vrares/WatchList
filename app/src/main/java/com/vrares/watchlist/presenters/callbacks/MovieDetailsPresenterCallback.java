package com.vrares.watchlist.presenters.callbacks;

public interface MovieDetailsPresenterCallback {

    void onSeenCountReceived(String seenCount);

    void onMovieMarkedAsFavourites();

    void onFavouriteStatusReturn(boolean isFavourite);

    void onMovieRemovedFromFavourites();
}
