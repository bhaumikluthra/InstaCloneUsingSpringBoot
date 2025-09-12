package com.instaApp.instaClone.controllers.models;

public class AddStatusApiReq {
    private final String userName;
    private final String status;
    AddStatusApiReq(String  userName,String status) {
        this.userName = userName;
        this.status = status;
    }
    public String getUserName() {
        return userName;
    }
    public String getStatus() {
        return status;
    }
}
