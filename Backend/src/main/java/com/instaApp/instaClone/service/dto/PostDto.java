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

import java.time.LocalDateTime;

public class PostDto {
    private int postId;         // add this

    private String postTitle;
    private String postContent;
    private String username;
    private LocalDateTime createdAt;  // <-- add this field


    public PostDto() {}
    // A no-argument constructor is standard for DTOs
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

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

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getCreatedAt() { return createdAt; }  // <-- getter
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}