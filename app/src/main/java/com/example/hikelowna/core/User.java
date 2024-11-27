package com.example.hikelowna.core;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String displayName;
    private String email;

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    private String passwordHash;
    private String profilePicture;
    private String bio;
    private String location;
    private String preferredDifficultyLevel;
    private List<String> favoriteHikes;
    private List<Hike> hikingHistory;
    private double totalHikingTime;
    private double totalHikingDistance;
    private List<String> achievements;
    private List<User> friends;
    private List<String> reviews;
    private List<String> groups;
    private boolean notificationsEnabled;
    private String privacySettings;
    private String preferredUnits;

    @Override
    public String toString() {
        return "User{" + "\n"  +
                "   username = " + (username != null ? username : "null") + "\n" +
                "   displayName = " + (displayName != null ? displayName : "null") + "\n"  +
                "   email = " + (email != null ? email : "null") + "\n"  +
                "   password = " + (passwordHash != null ? passwordHash : "null") + "\n"  +
                "   profilePicture = " + (profilePicture != null ? profilePicture : "null") + "\n"  +
                "   bio = " + (bio != null ? bio : "null") +  "\n"  +
                "   location =" + (location != null ? location : "null") + "\n"  +
                "   preferredDifficultyLevel =" + (preferredDifficultyLevel != null ? preferredDifficultyLevel : "null") + "\n"  +
                "   favoriteHikes = " + (favoriteHikes != null ? favoriteHikes : "null") + "\n"  +
                "   hikingHistory = " + (hikingHistory != null ? hikingHistory : "null") + "\n"  +
                "   totalHikingTime = " + totalHikingTime + "\n"  +
                "   totalHikingDistance = " + totalHikingDistance + "\n"  +
                "   achievements = " + (achievements != null ? achievements : "null") + "\n"  +
                "   friends = " + (friends != null ? friends : "null") + "\n"  +
                "   reviews = " + (reviews != null ? reviews : "null") + "\n"  +
                "   groups = " + (groups != null ? groups : "null") + "\n"  +
                "   notificationsEnabled = " + notificationsEnabled + "\n"  +
                "   privacySettings = " + (privacySettings != null ? privacySettings : "null") + '\'' + "\n"  +
                "   preferredUnits = " + (preferredUnits != null ? preferredUnits : "null") + "\n" + '}';
    }


    public User(){
        // Initialize all lists
        favoriteHikes = new ArrayList<>();
        hikingHistory = new ArrayList<>();
        friends = new ArrayList<>();
        reviews = new ArrayList<>();
        groups = new ArrayList<>();
    }


    public User(String username, String passwordHash, String email, String bio, String location, String preferredDifficultyLevel){
        setUsername(username);
        setPasswordHash(passwordHash);
        setEmail(email);
        setBio(bio);
        setLocation(location);
        setPreferredDifficultyLevel(preferredDifficultyLevel);
        favoriteHikes = new ArrayList<>();
        hikingHistory = new ArrayList<>();
        friends = new ArrayList<>();
        reviews = new ArrayList<>();
        groups = new ArrayList<>();
    }

    // Generated Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPreferredDifficultyLevel() {
        return preferredDifficultyLevel;
    }

    public void setPreferredDifficultyLevel(String preferredDifficultyLevel) {
        this.preferredDifficultyLevel = preferredDifficultyLevel;
    }

    public List<String> getFavoriteHikes() {
        return favoriteHikes;
    }

    public void setFavoriteHikes(List<String> favoriteHikes) {
        this.favoriteHikes = favoriteHikes;
    }

    public List<Hike> getHikingHistory() {
        return hikingHistory;
    }

    public void setHikingHistory(List<Hike> hikingHistory) {
        this.hikingHistory = hikingHistory;
    }

    public double getTotalHikingTime() {
        return totalHikingTime;
    }

    public void setTotalHikingTime(double totalHikingTime) {
        this.totalHikingTime = totalHikingTime;
    }

    public double getTotalHikingDistance() {
        return totalHikingDistance;
    }

    public void setTotalHikingDistance(double totalHikingDistance) {
        this.totalHikingDistance = totalHikingDistance;
    }

    public List<String> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<String> achievements) {
        this.achievements = achievements;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getPrivacySettings() {
        return privacySettings;
    }

    public void setPrivacySettings(String privacySettings) {
        this.privacySettings = privacySettings;
    }

    public String getPreferredUnits() {
        return preferredUnits;
    }

    public void setPreferredUnits(String preferredUnits) {
        this.preferredUnits = preferredUnits;
    }
}
