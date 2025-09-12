package com.instaApp.instaClone.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserDto {
    private final String userName;
    private final String name;
    private String bio;
    private String status;

    public  UserDto(String userName, String name) {
        this.userName = userName;
        this.name = name;
    }

    public  UserDto( String userName,String name,String bio) {
        this.userName = userName;
        this.name = name;
        this.bio = bio;
    }

    @JsonCreator
    public  UserDto(@JsonProperty("userName") String userName,@JsonProperty("name") String name,@JsonProperty String bio,@JsonProperty String status) {
        this.userName = userName;
        this.name = name;
        this.bio = bio;
        this.status = status;
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
}
