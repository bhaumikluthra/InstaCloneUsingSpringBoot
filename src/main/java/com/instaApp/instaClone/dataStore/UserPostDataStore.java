package com.instaApp.instaClone.dataStore;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserPostDataStore {
    Map<String, List<Integer>> userPostMap=new HashMap<>();


    public void addUserPost(String userName,Integer postId){
        userPostMap.computeIfAbsent(userName, k -> new ArrayList<>()).add(postId);
    }


    public void removeUserPost(String userName,Integer postId){
        if(userPostMap.containsKey(userName)) {
            userPostMap.get(userName).remove(postId);
        }
    }

    public List<Integer> getUserPost(String userName){
        return userPostMap.get(userName);
    }



}
