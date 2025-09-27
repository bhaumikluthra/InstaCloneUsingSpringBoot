package com.instaApp.instaClone.controllers.models;

public class AddBioApiReq {
    private final String bio;
    private final String userName;

    AddBioApiReq(String bio,String userName){
        this.bio=bio;
        this.userName=userName;
    }
    public String getBio() {
        return bio;
    }
    public String getUserName() {
        return userName;
    }

}
