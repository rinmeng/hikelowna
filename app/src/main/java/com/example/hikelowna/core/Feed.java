package com.example.hikelowna.core;

import java.util.ArrayList;
import java.util.List;

// A feed has a list of comments, and can either be a Review or a post,
public class Feed{
    private int likeCount;
    private int commentCount;
    private List<Comment> comments;
    private Post post;

    // Feed initializer
    public Feed(){
        this.likeCount = -1;
        this.commentCount = -1;
        this.comments = new ArrayList<>();
        this.post = new Post();
    }

    // Make a feed with a list of comments.
    public Feed(Post post, List<Comment> comments){
        this.likeCount = 0;
        this.commentCount = 0;
        this.post = new Post();
        this.comments = comments;
    }


    public void addFeedComment(Comment comment){
        this.comments.add(comment);
    }

    public void incrementLikes(){
        this.likeCount++;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", comments=" + comments +
                ", posts=" + post.toString() +
                '}';
    }


}
