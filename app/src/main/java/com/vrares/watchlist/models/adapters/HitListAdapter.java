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
import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.models.utils.DateFormatUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vrares.watchlist.android.activities.MovieDetailsActivity.BANNER_W1280;
import static com.vrares.watchlist.models.adapters.MovieListAdapter.POSTER_URL;

public class HitListAdapter extends RecyclerView.Adapter<HitListAdapter.MyViewHolder> {


    private ArrayList<HitMovie> movieList;
    private Context context;

    public HitListAdapter(ArrayList<HitMovie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
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

        DateFormatUtil dateFormatUtil = new DateFormatUtil("dd-MMM-yyyy HH:mm");
        holder.tvDate.setText(dateFormatUtil.getFormatDate(movie.getSeenDate()));

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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_hit_title)TextView tvTitle;
        @BindView(R.id.card_hit_backdrop)ImageView ivBackdrop;
        @BindView(R.id.card_seen_date)TextView tvDate;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateList(ArrayList<HitMovie> list) {
        movieList = list;
        notifyDataSetChanged();
    }
}
