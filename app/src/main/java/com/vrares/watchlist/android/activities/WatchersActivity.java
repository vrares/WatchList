package com.vrares.watchlist.android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.WatchersView;
import com.vrares.watchlist.models.adapters.WatchersAdapter;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.models.pojos.Watcher;
import com.vrares.watchlist.presenters.classes.WatchersPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.vrares.watchlist.android.activities.MovieDetailsActivity.BANNER_W1280;
import static com.vrares.watchlist.models.adapters.MovieListAdapter.POSTER_URL;

public class WatchersActivity extends AppCompatActivity implements WatchersView {

    @Inject WatchersPresenter watchersPresenter;
    @InjectExtra Movie movie;
    @BindView(R.id.iv_watchers_banner)ImageView ivWatchersBanner;
    @BindView(R.id.rv_watchers)RecyclerView rvWatchers;

    private Scope scope;
    private ArrayList<Watcher> watcherList;
    private WatchersAdapter watchersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchers);
        ButterKnife.bind(this);
        Dart.inject(this);
        scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
    }

    @Override
    protected void onStart() {
        super.onStart();
        watchersPresenter.attach(this);
        init();
    }

    @Override
    protected void onStop() {
        watchersPresenter.detach();
        super.onStop();
    }

    private void init() {
        watcherList = new ArrayList<>();
        Glide.with(this)
                .load(POSTER_URL + BANNER_W1280 + movie.getBackdropPath())
                .into(ivWatchersBanner);
        watchersPresenter.getWatchers(movie, watcherList);
    }

    @Override
    public void onWatchersReceived(ArrayList<Watcher> watcherList) {
        this.watcherList = watcherList;
        watchersAdapter = new WatchersAdapter(this, watcherList);
        rvWatchers.setAdapter(watchersAdapter);
        rvWatchers.setLayoutManager(new LinearLayoutManager(this));
        watchersAdapter.notifyDataSetChanged();
    }
}
