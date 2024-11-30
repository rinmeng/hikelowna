package com.example.hikelowna.core;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private final List<Hike> hikingHistory;
    private String username;
    private String displayName;
    private String email;
    private String passwordHash;
    private String profilePicture;
    private String bio;
    private String location;
    private String preferredDifficultyLevel;


    public User() {
        // Initialize all lists
        hikingHistory = new ArrayList<>();
    }

    public User(String username, String passwordHash, String email, String bio, String location, String preferredDifficultyLevel) {
        setUsername(username);
        setPasswordHash(passwordHash);
        setEmail(email);
        setBio(bio);
        setLocation(location);
        setPreferredDifficultyLevel(preferredDifficultyLevel);
        // Initialize all lists
        hikingHistory = new ArrayList<>();
    }

    public void addHikeToHistory(Hike hike) {
        hikingHistory.add(hike);
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

    public List<Hike> getHikingHistory() {
        return hikingHistory;
    }

    public String getPreferredDifficultyLevel() {
        return preferredDifficultyLevel;
    }

    public void setPreferredDifficultyLevel(String preferredDifficultyLevel) {
        this.preferredDifficultyLevel = preferredDifficultyLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "User{" + "\n" +
                "   username = " + (username != null ? username : "null") + "\n" +
                "   displayName = " + (displayName != null ? displayName : "null") + "\n" +
                "   email = " + (email != null ? email : "null") + "\n" +
                "   password = " + (passwordHash != null ? passwordHash : "null") + "\n" +
                "   profilePicture = " + (profilePicture != null ? profilePicture : "null") + "\n" +
                "   bio = " + (bio != null ? bio : "null") + "\n" +
                "   location =" + (location != null ? location : "null") + "\n" +
                "   preferredDifficultyLevel = " + (preferredDifficultyLevel != null ? preferredDifficultyLevel : "null") + "\n" +
                "   hikingHistory = " + (hikingHistory != null ? hikingHistory.toString() : "null");

    }


}
