package com.vrares.watchlist.android.fragments.friendsSubFragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.FriendsView;
import com.vrares.watchlist.models.adapters.FriendRequestPendingAdapter;
import com.vrares.watchlist.models.adapters.FriendsAdapter;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.classes.FriendsPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.vrares.watchlist.android.activities.LoginActivity.EMAIL_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.FIRST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.LAST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.PICTURE_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.SHARED_PREF;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendListFragment extends Fragment implements FriendsView{

    @BindView(R.id.rv_friends)RecyclerView rvFriends;
    @BindView(R.id.rv_pending)RecyclerView rvPending;
    @Inject FriendsPresenter friendsPresenter;

    private ArrayList<User> pendingList;
    private ArrayList<User> friendsList;
    private FriendsAdapter friendsAdapter;
    private FriendRequestPendingAdapter pendingAdapter;
    private User localUser;
    private SharedPreferences sharedPreferences;


    public FriendListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, view);
        Scope scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        friendsPresenter.attach(this);
        init();
    }

    @Override
    public void onStop() {
        super.onStop();
        friendsPresenter.detach();
    }

    private void init() {
        friendsList = new ArrayList<>();
        pendingList = new ArrayList<>();
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        localUser = new User(sharedPreferences.getString(FIRST_NAME_PREF, ""),
                sharedPreferences.getString(LAST_NAME_PREF, ""),
                sharedPreferences.getString(EMAIL_PREF, ""),
                sharedPreferences.getString(PICTURE_PREF, ""));
        localUser.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        friendsPresenter.getPendingAndFriendsList(pendingList, friendsList, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public void onPendingAndFriendsReceived(ArrayList<User> pendingList, ArrayList<User> friendsList) {
        this.pendingList = pendingList;
        this.friendsList = friendsList;

        friendsAdapter = new FriendsAdapter(friendsList, getContext());
        pendingAdapter = new FriendRequestPendingAdapter(pendingList, getContext(), localUser);
        rvFriends.setAdapter(friendsAdapter);
        rvPending.setAdapter(pendingAdapter);
        rvPending.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriends.setLayoutManager(new GridLayoutManager(getContext(), 2));
        pendingAdapter.notifyDataSetChanged();
        friendsAdapter.notifyDataSetChanged();

    }

}
