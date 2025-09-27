package com.instaApp.instaClone.controllers.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instaApp.instaClone.service.dto.PostDto;

import java.util.List;

public class GetAllPostApiRes {

    private final List<PostDto> posts;

    @JsonCreator
    public GetAllPostApiRes(@JsonProperty("posts") List<PostDto> posts){
        this.posts = posts;
    }

    public List<PostDto> getPosts() {
        return posts;
    }


}
