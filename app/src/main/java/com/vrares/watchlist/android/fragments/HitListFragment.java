package com.vrares.watchlist.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.HitListView;
import com.vrares.watchlist.models.adapters.HitListAdapter;
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.presenters.classes.HitListPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HitListFragment extends Fragment implements HitListView{

    @BindView(R.id.tv_hit_name)TextView ownerName;
    @BindView(R.id.rv_hit_list)RecyclerView rvHitList;
    @Inject HitListPresenter hitListPresenter;

    private ArrayList<Movie> hitList;
    private HitListAdapter hitListAdapter;

    public HitListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hit_list, container, false);
        ButterKnife.bind(this, view);
        Scope scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        init();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        hitListPresenter.attach(this);
        hitListPresenter.getHitList(hitList, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public void onStop() {
        hitListPresenter.detach();
        super.onStop();
    }

    public void init() {
        hitList = new ArrayList<>();
    }

    @Override
    public void onHitListReceived(ArrayList<Movie> hitList) {
        this.hitList = hitList;
        hitListAdapter = new HitListAdapter(hitList, getContext(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        rvHitList.setAdapter(hitListAdapter);
        rvHitList.setLayoutManager(new LinearLayoutManager(getContext()));
        hitListAdapter.notifyDataSetChanged();
        //// TODO: 11/24/2017 Test it
    }
}
