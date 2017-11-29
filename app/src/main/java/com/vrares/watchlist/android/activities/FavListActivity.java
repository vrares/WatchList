package com.vrares.watchlist.android.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.google.firebase.auth.FirebaseAuth;
import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.FavListView;
import com.vrares.watchlist.models.adapters.HitListAdapter;
import com.vrares.watchlist.models.pojos.HitMovie;
import com.vrares.watchlist.models.pojos.User;
import com.vrares.watchlist.presenters.classes.FavListPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

import static com.vrares.watchlist.android.activities.LoginActivity.FIRST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.LAST_NAME_PREF;
import static com.vrares.watchlist.android.activities.LoginActivity.SHARED_PREF;
import static com.vrares.watchlist.android.fragments.MovieListFragment.ACTION;
import static com.vrares.watchlist.android.fragments.MovieListFragment.ADVENTURE;
import static com.vrares.watchlist.android.fragments.MovieListFragment.ALL;
import static com.vrares.watchlist.android.fragments.MovieListFragment.ANIMATION;
import static com.vrares.watchlist.android.fragments.MovieListFragment.COMEDY;
import static com.vrares.watchlist.android.fragments.MovieListFragment.CRIME;
import static com.vrares.watchlist.android.fragments.MovieListFragment.DOCUMENTARY;
import static com.vrares.watchlist.android.fragments.MovieListFragment.DRAMA;
import static com.vrares.watchlist.android.fragments.MovieListFragment.FAMILY;
import static com.vrares.watchlist.android.fragments.MovieListFragment.FANTASY;
import static com.vrares.watchlist.android.fragments.MovieListFragment.HISTORY;
import static com.vrares.watchlist.android.fragments.MovieListFragment.HORROR;
import static com.vrares.watchlist.android.fragments.MovieListFragment.MUSIC;
import static com.vrares.watchlist.android.fragments.MovieListFragment.MYSTERY;
import static com.vrares.watchlist.android.fragments.MovieListFragment.ROMANCE;
import static com.vrares.watchlist.android.fragments.MovieListFragment.SF;
import static com.vrares.watchlist.android.fragments.MovieListFragment.THRILLER;
import static com.vrares.watchlist.android.fragments.MovieListFragment.TV;
import static com.vrares.watchlist.android.fragments.MovieListFragment.WAR;
import static com.vrares.watchlist.android.fragments.MovieListFragment.WESTERN;

public class FavListActivity extends AppCompatActivity implements FavListView{

    @BindView(R.id.tv_fav_name) TextView ownerName;
    @BindView(R.id.rv_fav_list) RecyclerView rvFavList;
    @BindView(R.id.pb_favlist) ProgressBar pbLoading;
    @BindView(R.id.fav_spinner_sort) Spinner spinnerSort;
    @BindView(R.id.fav_spinner_filter) Spinner spinnerFilter;
    @Inject FavListPresenter favListPresenter;
    @InjectExtra User user;

    private ArrayList<HitMovie> favList = new ArrayList<>();
    private ArrayList<HitMovie> fullFavList;
    private ArrayList<HitMovie> tempList = new ArrayList<>();
    private HitListAdapter favListAdapter;
    private SharedPreferences sharedPreferences;
    private boolean loading;
    private String sortMode;
    private int filterMode;
    private boolean isFiltered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_favourites);
        Scope scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        ButterKnife.bind(this);
        Dart.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        favListPresenter.attach(this);
        favList.clear();
        init();
        switchVisibility(loading);
        favListPresenter.getFavList(favList, user.getId());
    }

    @Override
    public void onStop() {
        favListPresenter.detach();
        super.onStop();
    }

    public void init() {
        loading = true;
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        ownerName.setText(sharedPreferences.getString(FIRST_NAME_PREF, "") + " " + sharedPreferences.getString(LAST_NAME_PREF, ""));
        favListAdapter = new HitListAdapter(this.favList, this);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortMode = spinnerSort.getSelectedItem().toString();
                ArrayList<HitMovie> sortList;
                if (!isFiltered) {
                    sortList = favList;
                } else {
                    sortList = tempList;
                }
                Collections.sort(sortList, new Comparator<HitMovie>() {
                    @Override
                    public int compare(HitMovie movie1, HitMovie movie2) {

                        if (movie1.getSeenDate() != null && movie2.getSeenDate() != null) {
                            Date date1 = new Date(Long.parseLong(movie1.getSeenDate()));
                            Date date2 = new Date(Long.parseLong(movie2.getSeenDate()));


                            switch (sortMode) {
                                case "Name asc.":
                                    return movie1.getMovie().getOriginalTitle().compareToIgnoreCase(movie2.getMovie().getOriginalTitle());
                                case "Name desc.":
                                    return movie2.getMovie().getOriginalTitle().compareToIgnoreCase(movie1.getMovie().getOriginalTitle());
                                case "Date asc.":
                                    return date1.compareTo(date2);
                                case "Date desc.":
                                    return date2.compareTo(date1);
                                default:
                                    return -1;
                            }
                        } else {
                            return -1;
                        }
                    }
                });
                favListAdapter.updateList(sortList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                favList = fullFavList;
                switch (spinnerFilter.getSelectedItem().toString()) {
                    case "Action":
                        filterMode = ACTION;
                        break;
                    case "Adventure":
                        filterMode = ADVENTURE;
                        break;
                    case "Animation":
                        filterMode = ANIMATION;
                        break;
                    case "Comedy":
                        filterMode = COMEDY;
                        break;
                    case "Crime":
                        filterMode = CRIME;
                        break;
                    case "Documentary":
                        filterMode = DOCUMENTARY;
                        break;
                    case "Drama":
                        filterMode = DRAMA;
                        break;
                    case "Family":
                        filterMode = FAMILY;
                        break;
                    case "Fantasy":
                        filterMode = FANTASY;
                        break;
                    case "History":
                        filterMode = HISTORY;
                        break;
                    case "Horror":
                        filterMode = HORROR;
                        break;
                    case "Music":
                        filterMode = MUSIC;
                        break;
                    case "Mystery":
                        filterMode = MYSTERY;
                        break;
                    case "Romance":
                        filterMode = ROMANCE;
                        break;
                    case "Science Fiction":
                        filterMode = SF;
                        break;
                    case "TV Movie":
                        filterMode = TV;
                        break;
                    case "Thriller":
                        filterMode = THRILLER;
                        break;
                    case "War":
                        filterMode = WAR;
                        break;
                    case "Western":
                        filterMode = WESTERN;
                        break;
                    case "All":
                        filterMode = ALL;
                        break;

                }

                boolean hasGenre = false;
                if (favList == null) {
                    favList = new ArrayList<>();
                } else {
                    favList = fullFavList;
                }
                tempList = new ArrayList<>();
                for (HitMovie movie : favList) {
                    isFiltered = true;
                    ArrayList<Integer> genreList = movie.getMovie().getGenreIds();
                    for (Integer genreId : genreList) {
                        if (genreId == filterMode) {
                            tempList.add(movie);
                            hasGenre = true;
                        } else if (filterMode == ALL) {
                            hasGenre = true;
                        }
                    }

                }
                if (hasGenre && filterMode == ALL){
                    favListAdapter.updateList(fullFavList);
                } else if (hasGenre) {
                    favListAdapter.updateList(tempList);
                } else if (filterMode != ALL) {
                    favListAdapter.updateList(new ArrayList<HitMovie>());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void switchVisibility(boolean loading) {
        if (loading) {
            pbLoading.setVisibility(View.VISIBLE);
            rvFavList.setVisibility(View.GONE);
        } else {
            pbLoading.setVisibility(View.GONE);
            rvFavList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFavListReceived(ArrayList<HitMovie> favList) {
        Collections.sort(favList, new Comparator<HitMovie>() {
            @Override
            public int compare(HitMovie movie1, HitMovie movie2) {
                return movie1.getMovie().getOriginalTitle().compareToIgnoreCase(movie2.getMovie().getOriginalTitle());
            }
        });
        loading = false;
        switchVisibility(loading);
        this.fullFavList = favList;
        this.favList = favList;
        rvFavList.setAdapter(favListAdapter);
        rvFavList.setLayoutManager(new GridLayoutManager(this, 2));
        favListAdapter.notifyDataSetChanged();
    }
}
