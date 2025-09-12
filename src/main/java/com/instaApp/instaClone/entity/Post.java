package com.instaApp.instaClone.entity;


public class Post {
    private static  int postId=0;
    private final String postTitle;
    private final String content;
    public Post(String postTitle, String content ) {
        postId++;
        this.postTitle=postTitle;
        this.content=content;
    }
    public String getPostTitle() {
        return postTitle;
    }
    public String getContent() {
        return content;
    }
    public  int getPostId() {
        return postId;
    }
}
