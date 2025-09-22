package com.instaApp.instaClone.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instaApp.instaClone.controllers.models.GetAllPostApiRes;
import com.instaApp.instaClone.controllers.models.UploadPostApiReq;
import com.instaApp.instaClone.service.PostService;
import com.instaApp.instaClone.service.dto.PostDto;

import jakarta.validation.Valid;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/uploadPost")
    public String uploadPost(@RequestBody @Valid UploadPostApiReq uploadPostApiReq, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "length of title should be 2-15 and post should be between 12 and 180";
        }
        PostDto postDto = new PostDto(uploadPostApiReq.getPostTitle(),uploadPostApiReq.getPostContent());
        return postService.addPost(postDto,uploadPostApiReq.getUserName());
    }

    @DeleteMapping("/delete/{useName}")
    public String deletePost(@RequestParam("postId") int postId,@PathVariable String userName){
        return postService.deletePost(postId,userName);
    }

    @GetMapping("/allpost/{userName}")
    public GetAllPostApiRes getAllPost(@PathVariable String userName){
        try {
            List<PostDto> posts = postService.getAllPostsByUser(userName);
            return new GetAllPostApiRes(posts);
        } catch (Exception e) {
            // Return empty list instead of throwing error
            return new GetAllPostApiRes(new ArrayList<>());
        }
    }

}
