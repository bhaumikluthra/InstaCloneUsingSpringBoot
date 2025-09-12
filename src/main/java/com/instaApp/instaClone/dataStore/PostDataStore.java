package com.instaApp.instaClone.dataStore;

import com.instaApp.instaClone.entity.Post;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostDataStore {
    Map<Integer, Post> postData =new HashMap<>();

    public void addPost(Post post){
        postData.put(post.getPostId(),post);
    }

    public void removePost(int postId){
        postData.remove(postId);
    }

    public void updatePost(Post post){
        postData.put(post.getPostId(),post);
    }
    public Post getPost(int postId){
        return  postData.get(postId);
    }
    public List<Post> getAllPost(){
        return  new ArrayList<>(postData.values());
    }




}
