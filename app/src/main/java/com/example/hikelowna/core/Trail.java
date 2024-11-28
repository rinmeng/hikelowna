package com.example.hikelowna.core;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Trail implements Comparable<Trail> {
    private String name;
    private String difficulty;
    private float length;
    private float estimatedTime;
    private float rating;
    private LatLng latLng;

    // Default constructor
    public Trail() {
        this.name = "";
        this.difficulty = "";
        this.length = -1.00f;
        this.estimatedTime = -1.00f;
        this.rating = 0.0f;
    }

    // Full constructor
    public Trail(String name, String difficulty, float length, float estimatedTime, float rating) {
        this.name = name;
        this.difficulty = difficulty;
        this.length = length;
        this.estimatedTime = estimatedTime;
        this.rating = rating;
    }

    @Override
    public int compareTo(Trail other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    // Method to generate difficulty stars
    private String getDifficultyStars() {
        String stars = "⬦⬦⬦⬦⬦";
        if (difficulty.equalsIgnoreCase("easy")) {
            stars = "⬥⬦⬦⬦⬦";
        } else if (difficulty.equalsIgnoreCase("moderate")) {
            stars = "⬥⬥⬦⬦⬦";
        } else if (difficulty.equalsIgnoreCase("difficult")) {
            stars = "⬥⬥⬥⬦⬦";
        } else if (difficulty.equalsIgnoreCase("extreme")) {
            stars = "⬥⬥⬥⬥⬦";
        }else if (difficulty.equalsIgnoreCase("impossible")) {
            stars = "⬥⬥⬥⬥⬥";
        }
        return stars;
    }

    // Method to generate rating stars
    private String getRatingStars() {
        return "★ " + this.rating;
    }

    // Shortened toString for list view
    public String toStringShort() {
        return getDifficultyStars() + " • "
                + getRatingStars() + " • "
                + length + "km • "
                + estimatedTime + "hr";
    }

    // Full toString for detailed view
    @Override
    public String toString() {
        return name + "\n"
                + getDifficultyStars() + " • " + getRatingStars() + "\n"
                + length + "km • " + estimatedTime + " hours\n";
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(float estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}