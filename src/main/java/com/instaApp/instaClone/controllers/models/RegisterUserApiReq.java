package com.instaApp.instaClone.controllers.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterUserApiReq {
//    @NotNull
    @Size(min = 4, max = 15, message = "please enter correct username")
    private  String userName;
    private  String name;

    @NotBlank
    @Size(min = 8)
    private String password;

    public RegisterUserApiReq(String userName,String name,String password) {
        this.userName = userName;
        this.name=name;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }
    public String getUserName() {
        return userName;
    }
    public  String getName() {
        return name;
    }
}
