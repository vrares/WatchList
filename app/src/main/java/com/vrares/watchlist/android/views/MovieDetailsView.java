package com.vrares.watchlist.android.views;

public interface MovieDetailsView {
    void onSeenCountReceived(String seenCount);

    void onMovieMarkedAsFavourites();

    void onFavouriteStatusReturn(boolean isFavourite);

    void onMovieRemovedFromFavourites();
}
