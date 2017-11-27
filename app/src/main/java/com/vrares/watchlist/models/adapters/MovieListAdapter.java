package com.vrares.watchlist.models.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.vrares.watchlist.android.activities.Henson;
import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.models.pojos.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> implements MovieListAdapterCallback {

    public static final String POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String LOGO_W45 = "w45/";

    private DatabaseHelper databaseHelper = new DatabaseHelper();

    private ArrayList<Movie> movieList;
    private Context context;
    private ProgressBar pbList;

    public MovieListAdapter(ArrayList<Movie> movieList, Context context, ProgressBar pbList) {
        this.movieList = movieList;
        this.context = context;
        this.pbList = pbList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);
        setItems(movie, holder);

        holder.buttonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.buttonItem.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.btn_seen).getConstantState()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("Are you sure you want to delete this movie from your seen list?");
                    alertDialog.setCancelable(true);

                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unseeButtonAction(movie, holder, position);
                            holder.buttonItem.setBackgroundResource(R.drawable.btn_unseen);
                        }
                    });

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                } else {
                    seenButtonAction(movie, holder, position);
                    holder.buttonItem.setBackgroundResource(R.drawable.btn_seen);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Henson.with(context)
                        .gotoMovieDetailsActivity()
                        .movie(movie)
                        .build();
                context.startActivity(intent);
            }
        });

    }

    private void unseeButtonAction(Movie movie, MyViewHolder holder, int position) {
        databaseHelper.unseeButtonAction(movie, this, holder, position);
    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    private void seenButtonAction(Movie movie, MyViewHolder holder, int position) {
        databaseHelper.seenButtonAction(movie, this, holder, position);
    }

    @Override
    public void onMovieSeen(int seenCount, MyViewHolder holder, int position) {
        notifyItemChanged(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_list_poster)
        public ImageView imageItem;
        @BindView(R.id.movie_list_title)
        public TextView titleItem;
        @BindView(R.id.movie_list_details)
        public TextView detailsItem;
        @BindView(R.id.movie_list_button)
        public Button buttonItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Observable<Movie> getObservable(Movie movie) {
        return Observable.just(movie);
    }

    private Observer<Movie> getObserver(final MyViewHolder holder) {
        return new Observer<Movie>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Movie movie) {
                databaseHelper.getSeenInformation(movie.getId(), holder, movie);

                holder.titleItem.setText(movie.getOriginalTitle());
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

    private void setItems(Movie movie, MyViewHolder holder) {
        getObservable(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(holder));
    }
}
