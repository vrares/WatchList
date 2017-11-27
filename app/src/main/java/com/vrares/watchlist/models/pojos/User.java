package com.vrares.watchlist.models.pojos;

import java.io.Serializable;

public class User implements Serializable{

    private String firstName;
    private String lastName;
    private String email;
    private String picture;
    private String id;

    public User(String firstName, String lastName, String email, String picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.picture = picture;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFullname() {
        return getFirstName() + " " + getLastName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
