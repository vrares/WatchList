package com.vrares.watchlist.models.pojos;

import java.io.Serializable;

public class Watcher implements Serializable{

    private String fullname;
    private String picture;
    private String id;
    private String time;
    private String email;

    public Watcher(String fullname, String picture, String id, String time) {
        this.fullname = fullname;
        this.picture = picture;
        this.id = id;
        this.time = time;
    }

    public Watcher() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
