package com.instaApp.instaClone.repository;

import com.instaApp.instaClone.dataStore.PostDataStore;
import com.instaApp.instaClone.dataStore.UserPostDataStore;
import com.instaApp.instaClone.entity.Post;
import com.instaApp.instaClone.service.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {
    @Autowired
    private PostDataStore postDataStore;
    @Autowired
    private UserPostDataStore userPostDataStore;
    public String addPost(PostDto postDto,String userName){
        Post currPost=new Post(postDto.getPostTitle(),postDto.getPostContent());
        postDataStore.addPost(currPost);
        userPostDataStore.addUserPost(userName,currPost.getPostId());
        return "New Post added with post id:"+currPost.getPostId();
    }
    public String deletePost(int postid,String userName){

        postDataStore.removePost(postid);
        userPostDataStore.removeUserPost(userName,postid);
        return "Post deleted";
    }

    public List<PostDto> getAllPost(){
        List<Post>ls=postDataStore.getAllPost();
        List<PostDto>ls1=new ArrayList<PostDto>();
        for(Post post:ls){
            ls1.add(new PostDto(post.getPostTitle(),post.getContent()));
        }
        return ls1;
    }

    public List<PostDto> getAllPostByUser(String userName){

        List<Integer>ls=userPostDataStore.getUserPost(userName);
        List<PostDto>ps=new  ArrayList<>();
        for(int i=0;i<ls.size();i++){
            PostDto j=new PostDto(postDataStore.getPost(ls.get(i)).getPostTitle(),postDataStore.getPost(ls.get(i)).getContent());
            ps.add(j);
        }
        return ps;

    }
}
