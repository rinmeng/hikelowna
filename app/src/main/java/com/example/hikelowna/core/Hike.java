package com.example.hikelowna.core;

import java.util.List;

public class Hike {
    private String hikeName;
    private String trailDescription;
    private String location;
    private String difficultyLevel; // Easy, Moderate, Hard
    private double length; // in kilometers or miles
    private double elevationGain; // in meters or feet
    private double estimatedDuration; // in hours
    private String trailType; // Loop, Out-and-back, etc.
    private String seasonality; // Best season for the hike
    private String trailConditions; // Trail conditions (rocky, muddy, etc.)
    private String startingPoint;
    private String endPoint;
    private double trailRating; // Average rating
    private List<String> photos; // List of URLs or paths to photos
    private String trailMap; // URL or link to map
    private List<String> completedBy; // Users who have completed the hike
    private List<String> reviews; // Reviews by users
    private String weatherConditions; // Typical or current weather
    private boolean dogFriendly; // Whether dogs are allowed

    // Getters and Setters for each field

    public String getHikeName() {
        return hikeName;
    }

    public void setHikeName(String hikeName) {
        this.hikeName = hikeName;
    }

    public String getTrailDescription() {
        return trailDescription;
    }

    public void setTrailDescription(String trailDescription) {
        this.trailDescription = trailDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(double elevationGain) {
        this.elevationGain = elevationGain;
    }

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(double estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getTrailType() {
        return trailType;
    }

    public void setTrailType(String trailType) {
        this.trailType = trailType;
    }

    public String getSeasonality() {
        return seasonality;
    }

    public void setSeasonality(String seasonality) {
        this.seasonality = seasonality;
    }

    public String getTrailConditions() {
        return trailConditions;
    }

    public void setTrailConditions(String trailConditions) {
        this.trailConditions = trailConditions;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public double getTrailRating() {
        return trailRating;
    }

    public void setTrailRating(double trailRating) {
        this.trailRating = trailRating;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getTrailMap() {
        return trailMap;
    }

    public void setTrailMap(String trailMap) {
        this.trailMap = trailMap;
    }

    public List<String> getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(List<String> completedBy) {
        this.completedBy = completedBy;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public boolean isDogFriendly() {
        return dogFriendly;
    }

    public void setDogFriendly(boolean dogFriendly) {
        this.dogFriendly = dogFriendly;
    }
}
