package com.vrares.watchlist.models.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vrares.watchlist.R;
import com.vrares.watchlist.models.pojos.PopularMovie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {

    private static final String POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String LOGO_W45  = "w45/";

    private ArrayList<PopularMovie> popularMovieList;
    private Context context;
    private ProgressBar pbList;

    public MovieListAdapter(ArrayList<PopularMovie> popularMovieList, Context context, ProgressBar pbList) {
        this.popularMovieList = popularMovieList;
        this.context = context;
        this.pbList = pbList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PopularMovie movie = popularMovieList.get(position);
        setItems(movie, holder);
    }

    @Override
    public int getItemCount() {
        return popularMovieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_list_poster) ImageView imageItem;
        @BindView(R.id.movie_list_title)TextView titleItem;
        @BindView(R.id.movie_list_details)TextView detailsItem;
        @BindView(R.id.movie_list_button)Button buttonItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Observable<PopularMovie> getObservable(PopularMovie movie) {
        return Observable.just(movie);
    }

    private Observer<PopularMovie> getObserver(final MyViewHolder holder) {
        return new Observer<PopularMovie>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PopularMovie movie) {
                holder.titleItem.setText(movie.getOriginalTitle());
                holder.detailsItem.setText(movie.getReleaseDate());
                Glide.with(context)
                        .load(POSTER_URL + LOGO_W45 + movie.getPosterPath())
                        .into(holder.imageItem);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                pbList.setVisibility(View.GONE);
            }
        };
    }

    private void setItems(PopularMovie popularMovie, MyViewHolder holder) {
        getObservable(popularMovie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(holder));
    }
}
