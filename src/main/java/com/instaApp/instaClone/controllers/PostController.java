package com.instaApp.instaClone.controllers;

import com.instaApp.instaClone.controllers.models.GetAllPostApiRes;
import com.instaApp.instaClone.controllers.models.UploadPostApiReq;
import com.instaApp.instaClone.service.PostService;
import com.instaApp.instaClone.service.dto.PostDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
@RestController
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
        return new GetAllPostApiRes(postService.getAllPostsByUser(userName));
    }

}
