package com.vrares.watchlist.android.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.MovieDetailsView;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.presenters.classes.MovieDetailsPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.vrares.watchlist.models.adapters.MovieListAdapter.POSTER_URL;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsView{

    public static final String BANNER_W1280 = "w1280/";
    private static final String POSTER_W780= "w780/";
    private static final String SHARE_URL = "https://www.themoviedb.org/movie/";

    @InjectExtra Movie movie;

    @Inject MovieDetailsPresenter movieDetailsPresenter;

    @BindView(R.id.iv_movie_banner)ImageView ivBanner;
    @BindView(R.id.iv_movie_poster)ImageView ivPoster;
    @BindView(R.id.tv_movie_title)TextView tvTitle;
    @BindView(R.id.fab_fav_btn)FloatingActionButton fabFavourite;
    @BindView(R.id.tv_rating)TextView tvRating;
    @BindView(R.id.tv_votes)TextView tvVotes;
    @BindView(R.id.tv_views)TextView tvView;
    @BindView(R.id.tv_language)TextView tvLanguage;
    @BindView(R.id.btn_other_watchers)Button btnOtherWatchers;
    @BindView(R.id.btn_more_details)Button btnMoreDetails;
    @BindView(R.id.tv_overview)TextView tvOverview;
    @BindView(R.id.pb_movie_details)ProgressBar pbMovieDetails;

    private Scope scope;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Dart.inject(this);
        ButterKnife.bind(this);
        scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
    }

    private void init() {
        tvTitle.setText(movie.getOriginalTitle());
        tvRating.setText(String.valueOf(movie.getVoteAverage()));
        tvVotes.setText(String.valueOf(movie.getVoteCount()));
        tvLanguage.setText(movie.getOriginalLanguage());
        tvOverview.setText(movie.getOverview());

        Glide.with(this)
                .load(POSTER_URL + BANNER_W1280 + movie.getBackdropPath())
                .into(ivBanner);

        Glide.with(this)
                .load(POSTER_URL + POSTER_W780 + movie.getPosterPath())
                .into(ivPoster);


    }

    @Override
    protected void onStart() {
        super.onStart();
        movieDetailsPresenter.attach(this);
        movieDetailsPresenter.getSeenCount(movie);
    }

    @Override
    protected void onStop() {
        movieDetailsPresenter.detach();
        super.onStop();
    }

    @OnClick(R.id.btn_more_details)
    public void openBrowserMoviePage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(SHARE_URL + movie.getId()));
        startActivity(intent);
    }

    @OnClick(R.id.btn_other_watchers)
    public void goToWatchersList() {
        Intent intent = Henson.with(this)
                .gotoWatchersActivity()
                .movie(movie)
                .build();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.movie_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, movie.getOriginalTitle());
                String shareLink = SHARE_URL + movie.getId();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
                startActivity(Intent.createChooser(shareIntent, "Share " + movie.getOriginalTitle()));
                break;
        }
        return true;
    }

    @Override
    public void onSeenCountReceived(String seenCount) {
        tvView.setText(seenCount);
        init();
        pbMovieDetails.setVisibility(View.GONE);
    }
}
