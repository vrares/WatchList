package com.vrares.watchlist.android.helpers;

import android.annotation.SuppressLint;

import com.vrares.watchlist.models.pojos.MovieList;
import com.vrares.watchlist.models.pojos.Session;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import static com.vrares.watchlist.android.helpers.RetrofitHelper.API_KEY_TAG;

public interface TmdbClient {

    String POPULAR_LIST = "/3/movie/popular";
    String GENRE_LIST = "/3/genre/";
    String SEARCH_LIST = "/3/search/movie";
    String SESSION_CALL = "/3/authentication/guest_session/new";

    @SuppressLint("LeadingSlashRetrofitApiEndpoint")
    @GET(POPULAR_LIST)
    Call<MovieList> getPopularMovieList(@QueryMap Map<String, String> filters);

    @GET(GENRE_LIST + "{genre}/movies")
    Call<MovieList> getGenreMovieList(@Path("genre") String genre, @QueryMap Map<String, String> filters);

    @SuppressLint("LeadingSlashRetrofitApiEndpoint")
    @GET(SEARCH_LIST)
    Call<MovieList> getSearchList(@QueryMap Map<String, String> filters);

    @SuppressLint("LeadingSlashRetrofitApiEndpoint")
    @GET(SESSION_CALL)
    Call<Session> createSession(@Query(API_KEY_TAG) String apiKey);

}
