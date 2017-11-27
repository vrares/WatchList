package com.vrares.watchlist.android.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.HitListView;
import com.vrares.watchlist.models.adapters.HitListAdapter;
import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.classes.HitListPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

public class HitListActivity extends AppCompatActivity implements HitListView{

    @Inject HitListPresenter hitListPresenter;

    @BindView(R.id.tv_hit_name_remote)TextView ownerName;
    @BindView(R.id.rv_hit_list_remote)RecyclerView rvHitList;
    @BindView(R.id.pb_hitlist_remote)ProgressBar pbLoading;

    @InjectExtra User user;

    private ArrayList<HitMovie> hitList;
    private HitListAdapter hitListAdapter;
    private boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_list);
        Scope scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        ButterKnife.bind(this);
        Dart.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        hitListPresenter.attach(this);
        init();
        switchVisibility(loading);
        hitListPresenter.getHitList(hitList, user.getId());
    }

    @Override
    protected void onStop() {
        hitListPresenter.detach();
        super.onStop();
    }

    private void init() {
        loading = true;
        hitList = new ArrayList<>();
        ownerName.setText(user.getFullname());
    }

    @Override
    public void onHitListReceived(ArrayList<HitMovie> hitList) {
        loading = false;
        switchVisibility(loading);
        this.hitList = hitList;
        hitListAdapter = new HitListAdapter(hitList, this);
        rvHitList.setAdapter(hitListAdapter);
        rvHitList.setLayoutManager(new GridLayoutManager(this, 2));
        hitListAdapter.notifyDataSetChanged();
    }

    public void switchVisibility(boolean loading) {
        if (loading) {
            pbLoading.setVisibility(View.VISIBLE);
            rvHitList.setVisibility(View.GONE);
        } else {
            pbLoading.setVisibility(View.GONE);
            rvHitList.setVisibility(View.VISIBLE);
        }
    }
}
