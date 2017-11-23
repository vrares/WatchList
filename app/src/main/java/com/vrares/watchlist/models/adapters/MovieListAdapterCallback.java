package com.vrares.watchlist.models.adapters;

public interface MovieListAdapterCallback {
    void onMovieSeen(int seenCount, MovieListAdapter.MyViewHolder holder, int position);

}
