package com.vrares.watchlist.android.fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.UserSearchView;
import com.vrares.watchlist.models.adapters.UserListAdapter;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.classes.UserSearchPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSearchFragment extends Fragment implements UserSearchView{

    @BindView(R.id.rv_users)RecyclerView rvUsers;
    @BindView(R.id.tv_user_search_title)TextView userSearchTitle;
    @Inject UserSearchPresenter userSearchPresenter;

    private ArrayList<User> usersList;
    private UserListAdapter userListAdapter;

    public UserSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_search, container, false);
        ButterKnife.bind(this, view);
        Scope scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        userSearchPresenter.attach(this);
        init();
    }

    @Override
    public void onStop() {
        userSearchPresenter.detach();
        super.onStop();
    }

    private void init() {
        usersList = new ArrayList<>();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_user);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userSearchPresenter.searchForUser(query.trim().toLowerCase(), usersList);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUserSearchComplete(ArrayList<User> usersList) {
        if (usersList.isEmpty()) {
            userSearchTitle.setTextColor(Color.RED);
            userSearchTitle.setText("Your entry does not match any of our users");
        } else {
            this.usersList = usersList;
            userListAdapter = new UserListAdapter(getContext(), this.usersList);
            rvUsers.setAdapter(userListAdapter);
            rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
            userListAdapter.notifyDataSetChanged();

            rvUsers.setVisibility(View.VISIBLE);
            userSearchTitle.setVisibility(View.GONE);
        }
    }
}
