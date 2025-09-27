//package com.instaApp.instaClone.controllers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.instaApp.instaClone.controllers.models.GetAllPostApiRes;
//import com.instaApp.instaClone.controllers.models.UploadPostApiReq;
//import com.instaApp.instaClone.service.PostService;
//import com.instaApp.instaClone.service.dto.PostDto;
//
//import jakarta.validation.Valid;
//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//public class PostController {
//
//    @Autowired
//    PostService postService;
//
//    @PostMapping("/uploadPost")
//    public String uploadPost(@RequestBody @Valid UploadPostApiReq uploadPostApiReq, BindingResult bindingResult) {
//        if(bindingResult.hasErrors()) {
//            return "length of title should be 2-15 and post should be between 12 and 180";
//        }
//        PostDto postDto = new PostDto(uploadPostApiReq.getPostTitle(),uploadPostApiReq.getPostContent());
//        return postService.addPost(postDto,uploadPostApiReq.getUserName());
//    }
//
//    @DeleteMapping("/delete/{useName}")
//    public String deletePost(@RequestParam("postId") int postId,@PathVariable String userName){
//        return postService.deletePost(postId,userName);
//    }
//
//    @GetMapping("/allpost/{userName}")
//    public GetAllPostApiRes getAllPost(@PathVariable String userName){
//        try {
//            List<PostDto> posts = postService.getAllPostsByUser(userName);
//            return new GetAllPostApiRes(posts);
//        } catch (Exception e) {
//            // Return empty list instead of throwing error
//            return new GetAllPostApiRes(new ArrayList<>());
//        }
//    }
//
//}
package com.instaApp.instaClone.controller;

import com.instaApp.instaClone.entity.Post;
import com.instaApp.instaClone.service.PostService;
import com.instaApp.instaClone.service.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Creates a new post for a specific user.
     * The URL is more RESTful: it identifies the user resource, then the posts collection.
     * POST /api/users/{username}/posts
     */
    // Inside PostController.java

    @PostMapping("/users/{username}/posts")
    public ResponseEntity<PostDto> createPost(@PathVariable String username, @RequestBody PostDto postDto) {
        PostDto createdPostDto = postService.addPost(postDto, username);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    /**
     * Deletes a post by its ID.
     * The URL directly identifies the post to be deleted.
     * DELETE /api/posts/{postId}
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable int postId) {
        // The service method now only needs the postId
        String result = postService.deletePost(postId);
        // Return a 200 OK status with the confirmation message
        return ResponseEntity.ok(result);
    }

    /**
     * Gets all posts in the system.
     * GET /api/posts
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Gets all posts created by a specific user.
     * GET /api/users/{username}/posts
     */
    @GetMapping("/users/{username}/posts")
    public ResponseEntity<List<PostDto>> getAllPostsByUser(@PathVariable String username) {
        List<PostDto> posts = postService.getAllPostsByUser(username);
        return ResponseEntity.ok(posts);
    }
}