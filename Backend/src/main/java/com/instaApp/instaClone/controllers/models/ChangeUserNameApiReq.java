package com.instaApp.instaClone.controllers.models;

public class ChangeUserNameApiReq {
    private final String userName;
    private final String newUserName;
    ChangeUserNameApiReq(String userName,String newUserName){
        this.userName=userName;
        this.newUserName=newUserName;
    }
    public String getUserName() {
        return userName;
    }

    public String getNewUserName() {
        return newUserName;
    }
}
