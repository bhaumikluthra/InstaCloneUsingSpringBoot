package com.instaApp.instaClone.controllers.models;

public class ChangeNameApiReq {
    private final String userName;
    private final String newName;
    ChangeNameApiReq(String userName,String newName){
        this.userName = userName;
        this.newName = newName;
    }
    public String getUserName(){
        return userName;
    }
    public String getNewName(){
        return newName;
    }
}
