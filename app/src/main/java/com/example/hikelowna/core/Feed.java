package com.example.hikelowna.core;

import androidx.annotation.NonNull;

import java.io.Serializable;

// A feed has a list of comments, and can either be a Review or a post,
public class Feed implements Serializable {
    private Hike hike;
    private User poster;

    // Feed initializer
    public Feed() {
        this.hike = new Hike();
        this.poster = new User();
    }

    // Make a feed with a list of comments.
    public Feed(Hike hike, User poster) {
        this.hike = hike;
        this.poster = poster;
    }

    public Feed(Hike hike) {
        this.hike = hike;
    }

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public Hike getHike() {
        return hike;
    }

    public void setHike(Hike hike) {
        this.hike = hike;
    }

    @NonNull
    @Override
    public String toString() {
        return "Hike: " + hike.getHikeName() + "\n" +
                "Poster: " + poster.getUsername();
    }
}
