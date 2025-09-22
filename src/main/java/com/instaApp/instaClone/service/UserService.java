package com.instaApp.instaClone.service;


import com.instaApp.instaClone.repository.UserRepository;
import com.instaApp.instaClone.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public String addUser(UserDto user){
        return userRepository.addUser(user);
    }

    public String addBio(String bio,String userName){
        return userRepository.addBio(bio,userName);
    }

    public String deleteBio(String userName){
        return userRepository.deleteBio(userName);
    }

    public String changeBio(String bio,String userName){
        return userRepository.changeBio(bio,userName);
    }

    public String addStatus(String userName,String status){
        return userRepository.addStatus(userName,status);
    }

    public String deleteStatus(String userName){
        return userRepository.deleteStatus(userName);
    }



    public String changeUserName(String currUsername,String newUserName){
        return userRepository.changeUserName(currUsername,newUserName);
    }

    public String changeName(String userName,String name){
        return userRepository.changeName(userName,name);
    }

    public String addFollower(String tbf,String ci){
        return userRepository.followUser(tbf, ci);
    }

    public String removeFollower(String tbuf,String ci){
        return userRepository.unfollowUser(tbuf, ci);
    }

    public String block(String userName,String userToBeBlocked){
        return userRepository.block(userName,userToBeBlocked);
    }

    public String unblock(String userName,String userToBeUnBlocked){
        return userRepository.unBlockUser(userName,userToBeUnBlocked);
    }



    public List<String> getAllFollowers(String userName){
        return userRepository.getAllFollowers(userName);
    }

    public  List<String> getAllFollowing(String userName){
        return userRepository.getAllFollowing(userName);
    }

    public List<UserDto> getAllUsers(){
        return userRepository.getAllUsers();
    }





}
