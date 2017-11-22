package com.vrares.watchlist.android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.helpers.RetrofitHelper;
import com.vrares.watchlist.android.views.SplashView;
import com.vrares.watchlist.models.pojos.Session;

import javax.inject.Inject;

import static com.vrares.watchlist.android.activities.LoginActivity.SESSION_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.SHARED_PREF;

public class SplashActivity extends AppCompatActivity implements SplashView {

    @Inject RetrofitHelper retrofitHelper;
    private SplashView splashView = this;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
            startActivity(intent);
            finish();
        } else {
            retrofitHelper.createSession(splashView);
        }

    }

    @Override
    public void onSessionCreated(Session session) {

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(SESSION_PREF, session.getGuestSessionId())
                .apply();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
