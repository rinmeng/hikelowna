package com.example.hikelowna.core;

import java.io.Serializable;

public class Hike implements Serializable {
    private String hikeDescription;
    private String hikeName;
    private String elapsedTime;
    private String trailName;
    private String trailDifficultyStars;
    private String trailRatingStars;
    private float trailLength;

    public Hike() {
        hikeName = "myHike";
        hikeDescription = "myHikeDescription";
    }

    public Hike(String hikeName, String hikeDescription) {
        this.hikeName = hikeName;
        this.hikeDescription = hikeDescription;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    public String getTrailDifficultyStars() {
        return trailDifficultyStars;
    }

    public void setTrailDifficultyStars(String trailDifficultyStars) {
        this.trailDifficultyStars = trailDifficultyStars;
    }

    public String getTrailRatingStars() {
        return trailRatingStars;
    }

    public void setTrailRatingStars(String trailRatingStars) {
        this.trailRatingStars = trailRatingStars;
    }

    public float getTrailLength() {
        return trailLength;
    }

    public void setTrailLength(float trailLength) {
        this.trailLength = trailLength;
    }

    public String getHikeName() {
        return hikeName;
    }

    public void setHikeName(String hikeName) {
        this.hikeName = hikeName;
    }

    public String getHikeDescription() {
        return hikeDescription;
    }

    public void setHikeDescription(String hikeDescription) {
        this.hikeDescription = hikeDescription;
    }

    public void printHikeDetails() {
        System.out.println("Hike Name: " + hikeName);
        System.out.println("Hike Description: " + hikeDescription);
    }


}
