//package com.instaApp.instaClone.service;
//
//import com.instaApp.instaClone.repository.PostRepository;
//import com.instaApp.instaClone.service.dto.PostDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class PostService {
//    @Autowired
//    PostRepository postRepository;
//
//    public String addPost(PostDto postDto,String userName){
//        return postRepository.addPost(postDto,userName);
//
//    }
//
//    public String deletePost(int postid,String userName){
//        return postRepository.deletePost(postid,userName);
//    }
//
//    public List<PostDto> getAllPosts(){
//        return postRepository.getAllPost();
//    }
//
//    public List<PostDto> getAllPostsByUser(String userName){
//        return postRepository.getAllPostByUser(userName);
//    }
//
//}
package com.instaApp.instaClone.service;

import com.instaApp.instaClone.entity.Post;
import com.instaApp.instaClone.entity.User;
import com.instaApp.instaClone.repository.PostRepository;
import com.instaApp.instaClone.repository.UserRepository;
import com.instaApp.instaClone.service.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    // Inside PostService.java

    public PostDto addPost(PostDto postDto, String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Post postEntity = new Post();
        postEntity.setPostTitle(postDto.getPostTitle());
        postEntity.setContent(postDto.getPostContent());
        postEntity.setUser(user);

        Post savedPost = postRepository.save(postEntity); // Save the entity

        // Map the saved entity back to a DTO to return it
        PostDto savedPostDto = new PostDto();
        savedPostDto.setPostTitle(savedPost.getPostTitle());
        savedPostDto.setPostContent(savedPost.getContent());

        return savedPostDto;
    }

    public String deletePost(int postId) {
        if (!postRepository.existsById(postId)) {
            return "Error: Post with ID " + postId + " not found.";
        }
        postRepository.deleteById(postId);
        return "Post with ID " + postId + " has been deleted.";
    }

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        // Correctly map entities to DTOs using the no-arg constructor and setters
        return posts.stream().map(post -> {
            PostDto dto = new PostDto();
            dto.setPostTitle(post.getPostTitle());
            dto.setPostContent(post.getContent());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<PostDto> getAllPostsByUser(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        // Correctly map entities to DTOs using the no-arg constructor and setters
        return user.getPosts().stream().map(post -> {
            PostDto dto = new PostDto();
            dto.setPostTitle(post.getPostTitle());
            dto.setPostContent(post.getContent());
            return dto;
        }).collect(Collectors.toList());
    }
}