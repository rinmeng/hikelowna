package com.example.hikelowna.core;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {

    private User author;
    private String content;
    private String imageUrl;
    private Date timestamp;

    public Post(){
        this.author = new User();
        this.content = "";
        this.imageUrl = "";
        this.timestamp = new Date();
    }

    public Post(User author, String content, String imageUrl, Date timestamp){
        this.author = author;
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "author = " + (author != null ? author : "null") + "\n" +
                "content = " + (content != null ? content : "null") + "\n" +
                "imageUrl = " + (imageUrl != null ? imageUrl : "null") + "\n" +
                "timestamp = " + (timestamp != null ? timestamp.toString() : "null");
    }


}
