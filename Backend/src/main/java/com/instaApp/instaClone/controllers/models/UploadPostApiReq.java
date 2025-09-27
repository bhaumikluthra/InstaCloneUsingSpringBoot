package com.instaApp.instaClone.controllers.models;

import jakarta.validation.constraints.Size;

public class UploadPostApiReq {
    @Size(min=2,max =15, message = "Title invalid")
    private final String postTitle;
    @Size(min=12,max=180,message = "content should be between 12-180 words only")
    private final String postContent;
    private static String userName;

    public UploadPostApiReq(String postTitle, String postContent, String userName) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.userName = userName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getUserName() {
        return userName;
    }

}
