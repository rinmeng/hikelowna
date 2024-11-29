package com.example.hikelowna.core;

import androidx.annotation.NonNull;

public class Comment {

    private User commenter;
    private String content;

    public Comment(){
        this.commenter = new User();
        this.content = "";
    }

    public Comment(User commenter, String content){
        this.commenter = commenter;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    @NonNull
    @Override
    public String toString() {
        return "commenter: " + commenter.getUsername() + "\n"
                + "content: " + content;
    }
}
