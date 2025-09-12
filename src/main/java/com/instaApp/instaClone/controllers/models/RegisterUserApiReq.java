package com.instaApp.instaClone.controllers.models;


import jakarta.validation.constraints.Size;

public class RegisterUserApiReq {
//    @NotNull
    @Size(min = 4, max = 15, message = "please enter correct username")
    private  String userName;
    private  String name;

    @Size(min = 7,max = 15, message = "please add password between 7 to 15 alphabets")
    private String password;

    public RegisterUserApiReq(String userName,String name,String password) {
        this.userName = userName;
        this.name=name;
        this.password=password;
    }
    public String getUserName() {
        return userName;
    }
    public  String getName() {
        return name;
    }
}
