package com.instaApp.instaClone.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserDto {
    private final String userName;
    private final String name;
    private String bio;
    private String status;
    private String password;

    public  UserDto(String userName, String name,String password) {
        this.userName = userName;
        this.name = name;
        this.password = password;
    }

    public  UserDto( String userName,String name,String bio,String password) {
        this.userName = userName;
        this.name = name;
        this.bio = bio;
        this.password = password;
    }

    @JsonCreator
    public  UserDto(@JsonProperty("userName") String userName,@JsonProperty("name") String name,@JsonProperty String bio,@JsonProperty String status,@JsonProperty String password) {
        this.userName = userName;
        this.name = name;
        this.bio = bio;
        this.status = status;
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setBio(String bio){
        this.bio=bio;
    }
    public String getBio(){
        return bio;
    }
    public String getName(){
        return name;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {return password;}
}
