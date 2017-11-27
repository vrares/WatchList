package com.vrares.watchlist.models.pojos;

public class HitMovie {

    private Movie movie;
    private String seenDate;

    public HitMovie(Movie movie, String seenDate) {
        this.movie = movie;
        this.seenDate = seenDate;
    }

    public HitMovie() {
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getSeenDate() {
        return seenDate;
    }

    public void setSeenDate(String seenDate) {
        this.seenDate = seenDate;
    }
}
