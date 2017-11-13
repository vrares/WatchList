package com.vrares.watchlist.android.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vrares.watchlist.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Filter the activities
            }
        }, SPLASH_TIME_OUT);

    }
}
