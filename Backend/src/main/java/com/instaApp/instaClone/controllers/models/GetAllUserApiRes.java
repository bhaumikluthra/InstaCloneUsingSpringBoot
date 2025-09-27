package com.instaApp.instaClone.controllers.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instaApp.instaClone.service.dto.UserDto;

import java.util.List;

public class GetAllUserApiRes {

    private final List<UserDto> users;

    @JsonCreator
    public GetAllUserApiRes(@JsonProperty("users") List<UserDto> users){
        this.users = users;
    }

    public List<UserDto> getUsers() {
        return users;
    }

}
