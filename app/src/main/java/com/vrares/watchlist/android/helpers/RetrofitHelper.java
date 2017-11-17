package com.vrares.watchlist.android.helpers;

import android.util.Log;

import com.vrares.watchlist.models.pojos.PopularMovie;
import com.vrares.watchlist.models.pojos.PopularMovieList;
import com.vrares.watchlist.presenters.callbacks.MovieListPresenterCallback;
import com.vrares.watchlist.presenters.classes.MovieListPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class RetrofitHelper {

    private static final String TAG = "Error";
    private static final String BASE_URL = "https://api.themoviedb.org/";
    private static final String API_KEY_TAG = "api_key";
    private static final String API_KEY = "3923714031c68cb13bffea56cc6a9430";
    private static final String LANGUAGE_TAG = "language";
    private static final String LANGUAGE = "en-US";
    private static final String PAGE_TAG = "page";
    private static final String INCLUDE_ADULT_TAG = "include_adult";
    private static final String INCLUDE_ADULT = "true";
    private static final String SORT_BY_TAG = "sort_by";
    private static final String SORT_ASC = "created_at.asc";

    private MovieListPresenterCallback movieListPresenterCallback;

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getPopularMovieList(int pageNumber, MovieListPresenterCallback movieListCallback) {
        this.movieListPresenterCallback = movieListCallback;
        TmdbClient tmdbClient = getRetrofitInstance().create(TmdbClient.class);

        Map<String, String> data = new HashMap<>();
        data.put(API_KEY_TAG, API_KEY);
        data.put(LANGUAGE_TAG, LANGUAGE);
        data.put(PAGE_TAG, String.valueOf(pageNumber));

        Call<PopularMovieList> call = tmdbClient.getPopularMovieList(data);
        call.enqueue(new Callback<PopularMovieList>() {
            @Override
            public void onResponse(Call<PopularMovieList> call, Response<PopularMovieList> response) {
                if (response.isSuccessful()) {
                    ArrayList<PopularMovie> popularMovieList = response.body().getResults();
                    movieListPresenterCallback.onPopularMoviesRetrieved(popularMovieList);
                } else {
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<PopularMovieList> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    public void getMovieListByGenre(int genre, int pageNumber, MovieListPresenterCallback movieListCallback) {
        this.movieListPresenterCallback = movieListCallback;
        TmdbClient tmdbClient = getRetrofitInstance().create(TmdbClient.class);

        Map<String, String> data = new HashMap<>();
        data.put(API_KEY_TAG, API_KEY);
        data.put(LANGUAGE_TAG, LANGUAGE);
        data.put(INCLUDE_ADULT_TAG, INCLUDE_ADULT);
        data.put(SORT_BY_TAG, SORT_ASC);
        data.put(PAGE_TAG, String.valueOf(pageNumber));

        Call<PopularMovieList> call = tmdbClient.getGenreMovieList(String.valueOf(genre), data);
        call.enqueue(new Callback<PopularMovieList>() {
            @Override
            public void onResponse(Call<PopularMovieList> call, Response<PopularMovieList> response) {
                if (response.isSuccessful()) {
                    ArrayList<PopularMovie> movieList = response.body().getResults();
                    movieListPresenterCallback.onGenreMoviesRetrieved(movieList);
                } else {
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<PopularMovieList> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
