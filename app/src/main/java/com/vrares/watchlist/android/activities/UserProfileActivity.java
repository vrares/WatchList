package com.vrares.watchlist.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.UserProfileView;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.classes.UserProfilePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import toothpick.Scope;
import toothpick.Toothpick;

public class UserProfileActivity extends AppCompatActivity implements UserProfileView{

    @Inject UserProfilePresenter userProfilePresenter;

    @BindView(R.id.civ_user_picture)CircleImageView civPicture;
    @BindView(R.id.tv_user_name)TextView tvName;
    @BindView(R.id.tv_user_email)TextView tvEmail;
    @BindView(R.id.btn_user_chat)Button btnChat;
    @BindView(R.id.btn_user_favourite)Button btnFavourites;
    @BindView(R.id.btn_user_hit_list)Button btnHitList;
    @BindView(R.id.btn_users_friend)Button btnFriend;

    @InjectExtra User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        Dart.inject(this);
        Scope scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
    }

    @Override
    protected void onStart() {
        userProfilePresenter.attach(this);
        init();
        super.onStart();
    }

    @Override
    protected void onStop() {
        userProfilePresenter.detach();
        super.onStop();
    }

    @OnClick(R.id.btn_user_hit_list)
    public void goToHitList() {
        Intent intent = Henson.with(this)
                .gotoHitListActivity()
                .user(user)
                .build();
        startActivity(intent);
    }

    @OnClick(R.id.btn_user_favourite)
    public void goToFavList() {
        Intent intent = Henson.with(this)
                .gotoFavListActivity()
                .user(user)
                .build();
        startActivity(intent);
    }

    private void init() {
        Glide.with(this)
                .load(user.getPicture())
                .into(civPicture);
        tvName.setText(user.getFullname());
        tvEmail.setText(user.getEmail());
        if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            btnFriend.setVisibility(View.GONE);
        }
    }
}
