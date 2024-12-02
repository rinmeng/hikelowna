package com.example.hikelowna.core;

import java.io.Serializable;

public class Hike implements Serializable {
    private String hikeDescription;
    private String hikeName;
    private String elapsedTime;
    private String trailName;
    private int trailDifficultyStars;
    private int trailRating;
    private float trailLength;
    private float trailEstimatedTime;

    public Hike() {
        hikeName = "myHike";
        hikeDescription = "myHikeDescription";
    }

    public Hike(String hikeName, String hikeDescription) {
        this.hikeName = hikeName;
        this.hikeDescription = hikeDescription;
    }

    public Hike(String hikeName, String hikeDescription, String elapsedTime, String trailName, int trailDifficultyStars, int trailRating, float trailLength, float trailEstimatedTime) {
        this.hikeName = hikeName;
        this.hikeDescription = hikeDescription;
        this.elapsedTime = elapsedTime;
        this.trailName = trailName;
        this.trailDifficultyStars = trailDifficultyStars;
        this.trailRating = trailRating;
        this.trailLength = trailLength;
        this.trailEstimatedTime = trailEstimatedTime;
    }

    public float getTrailEstimatedTime() {
        return trailEstimatedTime;
    }

    public void setTrailEstimatedTime(float trailEstimatedTime) {
        this.trailEstimatedTime = trailEstimatedTime;
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

    public int getTrailDifficultyStars() {
        return trailDifficultyStars;
    }

    public void setTrailDifficultyStars(int trailDifficultyStars) {
        this.trailDifficultyStars = trailDifficultyStars;
    }

    public int getTrailRating() {
        return trailRating;
    }

    public void setTrailRating(int trailRating) {
        this.trailRating = trailRating;
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
