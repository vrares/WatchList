package com.vrares.watchlist.android.helpers;

import com.vrares.watchlist.models.pojos.PopularMovieList;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface TmdbClient {

    String POPULAR_LIST = "/3/movie/popular";
    String GENRE_LIST = "/3/genre/";

    @GET(POPULAR_LIST)
    Call<PopularMovieList> getPopularMovieList(@QueryMap Map<String, String> filters);

    @GET(GENRE_LIST + "{genre}/movies")
    Call<PopularMovieList> getGenreMovieList(@Path("genre") String genre, @QueryMap Map<String, String> filters);

}
