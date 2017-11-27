package com.vrares.watchlist.presenters.classes;

import com.vrares.watchlist.android.helpers.DatabaseHelper;
import com.vrares.watchlist.android.helpers.RetrofitHelper;
import com.vrares.watchlist.android.views.HitListView;
import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.presenters.callbacks.HitListPresenterCallback;

import java.util.ArrayList;

import javax.inject.Inject;

public class HitListPresenter implements HitListPresenterCallback{

    @Inject DatabaseHelper databaseHelper;
    @Inject RetrofitHelper retrofitHelper;

    private HitListView hitListView;

    public void attach(HitListView hitListView) {
        this.hitListView = hitListView;
    }

    public void detach() {
        this.hitListView = null;
    }

    public void getHitList(ArrayList<HitMovie> hitList, String uId) {
        databaseHelper.getHitList(hitList, uId, this);
    }

    @Override
    public void onHitListReceived(ArrayList<HitMovie> hitList) {
        if (hitListView != null) {
            hitListView.onHitListReceived(hitList);
        }
    }

    @Override
    public void onMoviesReceived(ArrayList<HitMovie> hitList, String uId) {
        databaseHelper.getSeenDate(hitList, this, uId);
    }
}
