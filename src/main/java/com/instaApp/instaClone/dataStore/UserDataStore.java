package com.instaApp.instaClone.dataStore;

import com.instaApp.instaClone.entity.User;
import com.instaApp.instaClone.service.dto.PostDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class UserDataStore
{
    Map<String, User> userdata=new HashMap<>();


    public void addUser(User user)
    {
        userdata.put(user.getUsername(),user);
    }

    public void removeUser(String userName)
    {
        userdata.remove(userName);
    }

    public void updateUser(User user)
    {
        userdata.put(user.getUsername(),user);
    }





    public String getBio(String userName){
        User user=userdata.get(userName);
        return user.getBio();
    }
    public List<String>blockedUsers(String userName)
    {
        User user=userdata.get(userName);
        return user.getBlockedUsers();
    }
    public User getUsername(String username){
        return userdata.get(username);
    }

    public String getName(String username){
        return userdata.get(username).getName();
    }



    public List<User> getAllUser(){
        return  userdata.values().stream().collect(Collectors.toList());
    }

}
