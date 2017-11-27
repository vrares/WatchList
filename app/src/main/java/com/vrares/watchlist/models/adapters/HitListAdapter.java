package com.vrares.watchlist.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.activities.Henson;
import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.models.pojos.Movie;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vrares.watchlist.android.activities.MovieDetailsActivity.BANNER_W1280;
import static com.vrares.watchlist.models.adapters.MovieListAdapter.POSTER_URL;

public class HitListAdapter extends RecyclerView.Adapter<HitListAdapter.MyViewHolder> implements HitListAdapterCallback{


    private ArrayList<HitMovie> movieList;
    private Context context;
    private String userId;

    public HitListAdapter(ArrayList<HitMovie> movieList, Context context, String userId) {
        this.movieList = movieList;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_hit_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final HitMovie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getMovie().getOriginalTitle());
        Glide.with(context)
                .load(POSTER_URL + BANNER_W1280 + movie.getMovie().getBackdropPath())
                .into(holder.ivBackdrop);
        holder.tvDate.setText(movie.getSeenDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Henson.with(context)
                        .gotoMovieDetailsActivity()
                        .movie(movie.getMovie())
                        .build();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public void onSeenDateReceived(String seenDate, MyViewHolder holder) {
        holder.tvDate.setText(seenDate);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_hit_title)TextView tvTitle;
        @BindView(R.id.card_hit_backdrop)ImageView ivBackdrop;
        @BindView(R.id.card_seen_date)TextView tvDate;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
