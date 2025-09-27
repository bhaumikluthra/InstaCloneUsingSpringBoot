//package com.instaApp.instaClone.dataStore;
//
//import com.instaApp.instaClone.entity.User;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import java.util.Set;
//
//@Component
//public class UserDataStore
//{
//    Map<String, User> userdata=new HashMap<>();
//
//
//    public void addUser(User user)
//    {
//        userdata.put(user.getUsername(),user);
//    }
//
//    public void removeUser(String userName)
//    {
//        userdata.remove(userName);
//    }
//
//    public void updateUser(User user)
//    {
//        userdata.put(user.getUsername(),user);
//    }
//
//
//
//
//
//    public String getBio(String userName){
//        User user=userdata.get(userName);
//        return user.getBio();
//    }
//    public List<String> getBlockedUsernames(String username) {
//        // 1. Find the user from the database
//        User user = UserRepository.findById(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 2. Get the Set<User> of blocked users from the entity
//        Set<User> blockedUsers = user.getBlockedUsers();
//
//        // 3. Use a stream to convert the Set<User> to a List<String>
//        return blockedUsers.stream()
//                .map(User::getUsername) // For each User object, get its username String
//                .collect(Collectors.toList()); // Collect all the usernames into a new List
//    }
//    public User getUsername(String username){
//        return userdata.get(username);
//    }
//
//    public String getName(String username){
//        return userdata.get(username).getName();
//    }
//
//
//
//    public List<User> getAllUser(){
//        return  userdata.values().stream().collect(Collectors.toList());
//    }
//
//}
