package com.vrares.watchlist;

import android.app.Application;

import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by rares.vultur on 11/13/2017.
 */

public class MyApplication extends Application{

    private static MyApplication instance;
    private Scope scope;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        scope = Toothpick.openScope(this);
        Toothpick.inject(this, scope);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
