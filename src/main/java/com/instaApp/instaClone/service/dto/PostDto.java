package com.instaApp.instaClone.service.dto;

public class PostDto {
    private final String postTitle;
    private final String postContent;

    public PostDto(String postTitle, String postContent) {
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostContent() {
        return postContent;
    }
}
