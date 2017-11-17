package com.vrares.watchlist.models.adapters;

public interface MovieListAdapterCallback {
    void onMovieSeen(int seenCount, MovieListMovieListAdapter.MyViewHolder holder, int position);

}
