package com.example.hikelowna.core;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private User reviewer;
    private String title;
    private String content;
    private int rating; // For example, 1 to 5
    private Date date;
    private String trailName; // e.g., Hike ID, Trail ID

    // Constructors
    public Review() {
        this.date = new Date(); // Set current date by default
    }

    public Review(User reviewer, String title, String content, int rating, String trailName) {
        this.reviewer = reviewer;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.date = new Date();
        this.trailName = trailName;
    }

    // Getters and Setters
    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if(rating < 1 || rating > 5){
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    @Override
    public String toString() {
        return "Review{" + "\n" +
                "   reviewer = " + (reviewer != null ? reviewer.getUsername() : "null") + "\n" +
                "   title = " + (title != null ? title : "null") + "\n" +
                "   content = " + (content != null ? content : "null") + "\n" +
                "   rating = " + rating + "\n" +
                "   date = " + (date != null ? date.toString() : "null") + "\n" +
                "   trailName = " + (trailName != null ? trailName : "null") + "\n" +
                '}';
    }
}