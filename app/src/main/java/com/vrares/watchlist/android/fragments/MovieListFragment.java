package com.vrares.watchlist.android.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.vrares.watchlist.MyApplication;
import com.vrares.watchlist.R;
import com.vrares.watchlist.android.views.MovieListView;
import com.vrares.watchlist.models.adapters.MovieListAdapter;
import com.vrares.watchlist.models.pojos.PopularMovie;
import com.vrares.watchlist.presenters.classes.MovieListPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements MovieListView {

    public static final int ALL = 0;
    public static final int ACTION = 28;
    public static final int ADVENTURE = 12;
    public static final int ANIMATION = 16;
    public static final int COMEDY = 35;
    public static final int CRIME = 80;
    public static final int DOCUMENTARY = 99;
    public static final int DRAMA = 18;
    public static final int FAMILY = 10751;
    public static final int FANTASY = 14;
    public static final int HISTORY = 36;
    public static final int HORROR = 27;
    public static final int MUSIC = 10402;
    public static final int MYSTERY = 9648;
    public static final int ROMANCE = 10749;
    public static final int SF = 878;
    public static final int TV = 10770;
    public static final int THRILLER = 53;
    public static final int WAR = 10752;
    public static final int WESTERN = 37;

    @Inject MovieListPresenter movieListPresenter;
    @BindView(R.id.pb_popular_movie_list) ProgressBar pbList;
    @BindView(R.id.rv_movie_list)RecyclerView rvMovieList;

    private int pageNumber;
    private int flag;
    private RecyclerView.OnScrollListener scrollListener;
    private ArrayList<PopularMovie> movieList;
    private Scope scope;
    private MovieListAdapter movieListAdapter;
    private LinearLayoutManager linearLayoutManager;

    public MovieListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);
        scope = Toothpick.openScopes(MyApplication.getInstance(), this);
        Toothpick.inject(this, scope);
        initViews();
        setHasOptionsMenu(true);
        recyclerViewListener();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        movieListPresenter.attach(this);
        rvMovieList.setVisibility(View.GONE);
        movieListPresenter.getPopularMovieList(pageNumber);
    }

    @Override
    public void onStop() {
        movieListPresenter.detach();
        super.onStop();
    }

    private void initViews() {
        pageNumber = 1;
        movieList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    public void onPopularMoviesRetrieved(ArrayList<PopularMovie> popularMovieList) {
        movieList.addAll(popularMovieList);

        movieListAdapter = new MovieListAdapter(movieList, getContext(), pbList);
        rvMovieList.setAdapter(movieListAdapter);
        rvMovieList.setLayoutManager(linearLayoutManager);
        movieListAdapter.notifyDataSetChanged();

        rvMovieList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_filters, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_all:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getPopularMovieList(pageNumber);
                flag = ALL;
                break;

            case R.id.filter_action:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(ACTION, movieList,  pageNumber);
                flag = ACTION;
                break;

            case R.id.filter_adventure:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(ADVENTURE, movieList,  pageNumber);
                flag = ADVENTURE;

                break;

            case R.id.filter_animation:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(ANIMATION, movieList,  pageNumber);
                flag = ANIMATION;

                break;

            case R.id.filter_comedy:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(COMEDY, movieList,  pageNumber);
                flag = COMEDY;

                break;

            case R.id.filter_crime:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(CRIME, movieList,  pageNumber);
                flag = CRIME;

                break;

            case R.id.filter_documentary:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(DOCUMENTARY, movieList,  pageNumber);
                flag = DOCUMENTARY;

                break;

            case R.id.filter_drama:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(DRAMA, movieList,  pageNumber);
                flag = DRAMA;

                break;

            case R.id.filter_family:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(FAMILY, movieList,  pageNumber);
                flag = FAMILY;

                break;

            case R.id.filter_fantasy:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(FANTASY, movieList,  pageNumber);
                flag = FANTASY;

                break;

            case R.id.filter_history:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(HISTORY, movieList,  pageNumber);
                flag = HISTORY;

                break;

            case R.id.filter_horror:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(HORROR, movieList,  pageNumber);
                flag = HORROR;

                break;

            case R.id.filter_Music:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(MUSIC, movieList,  pageNumber);
                flag = MUSIC;

                break;

            case R.id.filter_mystery:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(MYSTERY, movieList,  pageNumber);
                flag = MYSTERY;

                break;

            case R.id.filter_romance:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(ROMANCE, movieList,  pageNumber);
                flag = ROMANCE;

                break;

            case R.id.filter_sf:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(SF, movieList,  pageNumber);
                flag = SF;

                break;

            case R.id.filter_tv:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(TV, movieList,  pageNumber);
                flag = TV;

                break;

            case R.id.filter_thriller:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(THRILLER, movieList,  pageNumber);
                flag = THRILLER;

                break;

            case R.id.filter_war:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(WAR, movieList,  pageNumber);
                flag = WAR;

                break;

            case R.id.filter_western:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(WESTERN, movieList,  pageNumber);
                flag = WESTERN;

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void recyclerViewListener() {
        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemsCount = linearLayoutManager.getChildCount();
                int totalItemsCount = linearLayoutManager.getItemCount();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (dy < 0) {
                    if (firstVisibleItem > visibleItemsCount) {
                        //// TODO: 11/16/2017 set Scroll To Top Visible
                    } else {
                        //// TODO: 11/16/2017 Set Scroll To Top Gone
                    }
                } else {
                    //// TODO: 11/16/2017 Set Scroll To Top Gone

                    if (firstVisibleItem + visibleItemsCount >= totalItemsCount) {
                        pageNumber++;
                        if (flag == ALL) {
                            movieListPresenter.getPopularMovieList(pageNumber);
                        } else {
//                            movieListPresenter.getMovieListByGenre()
                        }
                    }

                    if (linearLayoutManager.findFirstVisibleItemPosition() < 2) {
                        //// TODO: 11/16/2017 Set Scroll To Top Gone
                    }
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvMovieList.setOnScrollListener(scrollListener);
        }
    }
}
