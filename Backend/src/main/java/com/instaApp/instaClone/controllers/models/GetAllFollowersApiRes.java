package com.instaApp.instaClone.controllers.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instaApp.instaClone.service.dto.PostDto;
import com.instaApp.instaClone.service.dto.UserDto;

import java.util.List;

public class GetAllFollowersApiRes {
    private  final List<UserDto> followers;
    @JsonCreator
    public GetAllFollowersApiRes(@JsonProperty("followers") List<UserDto> followers) {
        this.followers = followers;
    }
    public List<UserDto> getFollowers() {return followers;}
}
