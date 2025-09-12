package com.instaApp.instaClone.service;

import com.instaApp.instaClone.repository.PostRepository;
import com.instaApp.instaClone.service.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public String addPost(PostDto postDto,String userName){
        return postRepository.addPost(postDto,userName);

    }

    public String deletePost(int postid,String userName){
        return postRepository.deletePost(postid,userName);
    }

    public List<PostDto> getAllPosts(){
        return postRepository.getAllPost();
    }

    public List<PostDto> getAllPostsByUser(String userName){
        return postRepository.getAllPostByUser(userName);
    }

}
