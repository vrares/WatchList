package com.vrares.watchlist.android.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.vrares.watchlist.models.pojos.Movie;
import com.vrares.watchlist.presenters.classes.MovieListPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    public static final int SEARCH = 7777;

    @Inject MovieListPresenter movieListPresenter;
    @BindView(R.id.pb_popular_movie_list) ProgressBar pbList;
    @BindView(R.id.rv_movie_list)RecyclerView rvMovieList;
    @BindView(R.id.fab_scroll_to_top)FloatingActionButton fabScrollToTop;


    private int pageNumber;
    private int flag;
    private String searchQuery;
    private RecyclerView.OnScrollListener scrollListener;
    private ArrayList<Movie> movieList;
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
    public void onPopularMoviesRetrieved(ArrayList<Movie> movieList) {
        this.movieList.addAll(movieList);

        movieListAdapter = new MovieListAdapter(this.movieList, getContext(), pbList);
        rvMovieList.setAdapter(movieListAdapter);
        rvMovieList.setLayoutManager(linearLayoutManager);
        movieListAdapter.notifyItemRangeChanged(this.movieList.size() - movieList.size(), movieList.size());

        rvMovieList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGenreMoviesRetrieved(ArrayList<Movie> genreMovieList) {
        movieList.addAll(genreMovieList);

        movieListAdapter = new MovieListAdapter(movieList, getContext(), pbList);
        rvMovieList.setAdapter(movieListAdapter);
        rvMovieList.setLayoutManager(linearLayoutManager);
        movieListAdapter.notifyItemRangeChanged(movieList.size() - genreMovieList.size(), genreMovieList.size());

        pbList.setVisibility(View.GONE);
        rvMovieList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchMoviesRetrieved(ArrayList<Movie> movieList) {
        this.movieList.addAll(movieList);

        movieListAdapter = new MovieListAdapter(this.movieList, getContext(), pbList);
        rvMovieList.setAdapter(movieListAdapter);
        rvMovieList.setLayoutManager(linearLayoutManager);
        movieListAdapter.notifyItemRangeChanged(this.movieList.size() - movieList.size(), movieList.size());

        rvMovieList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query.replace(" ", "+");
                pbList.setVisibility(View.VISIBLE);
                rvMovieList.setVisibility(View.GONE);
                pageNumber = 1;
                flag = SEARCH;
                movieList.clear();
                movieListPresenter.searchMovie(searchQuery, pageNumber);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        inflater.inflate(R.menu.menu_filter, menu);
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
                movieListPresenter.getMovieListByGenre(ACTION, pageNumber);
                flag = ACTION;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_adventure:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(ADVENTURE, pageNumber);
                flag = ADVENTURE;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_animation:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(ANIMATION, pageNumber);
                flag = ANIMATION;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_comedy:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(COMEDY, pageNumber);
                flag = COMEDY;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_crime:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(CRIME, pageNumber);
                flag = CRIME;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_documentary:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(DOCUMENTARY, pageNumber);
                flag = DOCUMENTARY;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_drama:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(DRAMA, pageNumber);
                flag = DRAMA;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_family:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(FAMILY, pageNumber);
                flag = FAMILY;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_fantasy:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(FANTASY, pageNumber);
                flag = FANTASY;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_history:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(HISTORY, pageNumber);
                flag = HISTORY;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_horror:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(HORROR, pageNumber);
                flag = HORROR;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_Music:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(MUSIC, pageNumber);
                flag = MUSIC;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_mystery:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(MYSTERY, pageNumber);
                flag = MYSTERY;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_romance:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(ROMANCE, pageNumber);
                flag = ROMANCE;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_sf:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(SF, pageNumber);
                flag = SF;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_tv:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(TV, pageNumber);
                flag = TV;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_thriller:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(THRILLER, pageNumber);
                flag = THRILLER;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_war:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(WAR, pageNumber);
                flag = WAR;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

            case R.id.filter_western:
                pageNumber = 1;
                movieList.clear();
                movieListPresenter.getMovieListByGenre(WESTERN, pageNumber);
                flag = WESTERN;
                rvMovieList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_scroll_to_top)
    public void scrollToTop() {
        rvMovieList.setOnScrollListener(null);
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(0);
        linearLayoutManager.startSmoothScroll(smoothScroller);
        rvMovieList.setOnScrollListener(scrollListener);
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
                        fabScrollToTop.setVisibility(View.VISIBLE);
                    } else {
                        fabScrollToTop.setVisibility(View.GONE);
                    }
                } else {
                    fabScrollToTop.setVisibility(View.GONE);

                    if (firstVisibleItem + visibleItemsCount >= totalItemsCount) {
                        pageNumber++;
                        if (flag == ALL) {
                            movieListPresenter.getPopularMovieList(pageNumber);
                        } else if (flag == SEARCH) {
                            movieListPresenter.searchMovie(searchQuery, pageNumber);
                        } else {
                            movieListPresenter.getMovieListByGenre(flag, pageNumber);
                        }
                    }

                    if (linearLayoutManager.findFirstVisibleItemPosition() < 2) {
                        fabScrollToTop.setVisibility(View.GONE);
                    }
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvMovieList.setOnScrollListener(scrollListener);
        }
    }
}
