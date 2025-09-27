//package com.instaApp.instaClone.service.dto;
//
//public class PostDto {
//    private final String postTitle;
//    private final String postContent;
//
//    public PostDto(String postTitle, String postContent) {
//        this.postTitle = postTitle;
//        this.postContent = postContent;
//    }
//
//    public String getPostTitle() {
//        return postTitle;
//    }
//
//    public String getPostContent() {
//        return postContent;
//    }
//}

package com.instaApp.instaClone.service.dto;

public class PostDto {
    private String postTitle;
    private String postContent;

    // A no-argument constructor is standard for DTOs
    public PostDto() {
    }

    // Standard Getters and Setters for all fields

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}