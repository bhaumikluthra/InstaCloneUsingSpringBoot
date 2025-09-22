package com.instaApp.instaClone.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instaApp.instaClone.controllers.models.AddBioApiReq;
import com.instaApp.instaClone.controllers.models.AddStatusApiReq;
import com.instaApp.instaClone.controllers.models.ChangeNameApiReq;
import com.instaApp.instaClone.controllers.models.ChangeUserNameApiReq;
import com.instaApp.instaClone.controllers.models.GetAllUserApiRes;
import com.instaApp.instaClone.controllers.models.RegisterUserApiReq;
import com.instaApp.instaClone.service.UserService;
import com.instaApp.instaClone.service.dto.UserDto;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/adduser")
    public String registerUser(@RequestBody @Valid RegisterUserApiReq registerUserApiReq, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "username not valid";
        }
        UserDto userDto = new UserDto(registerUserApiReq.getUserName(),registerUserApiReq.getName(),registerUserApiReq.getPassword());
        return userService.addUser(userDto);
    }

    @PostMapping("/addBio")
    public String addBio(@RequestBody AddBioApiReq addBioApiReq){
        return userService.addBio(addBioApiReq.getBio(),addBioApiReq.getUserName());
    }

    @DeleteMapping("/deleteBio/{userName}")
    public String deleteBio(@PathVariable String userName){
        return userService.deleteBio(userName);
    }

    @PutMapping("/changeBio")
    public String changeBio(@RequestBody AddBioApiReq addBioApiReq){
        return userService.changeBio(addBioApiReq.getBio(),addBioApiReq.getUserName());
    }

    @PostMapping("/addStatus")
    public String addStatus(@RequestBody AddStatusApiReq addStatusApiReq){
        return userService.addStatus(addStatusApiReq.getUserName(),addStatusApiReq.getStatus());
    }

    @DeleteMapping("/deleteStatus/{userName}")
    public String deleteStatus(@PathVariable String userName){
        return userService.deleteStatus(userName);
    }


    @PutMapping("/changeUserName")
    public String changeUserName(@RequestBody ChangeUserNameApiReq changeUserNameApiReq){
        return userService.changeUserName(changeUserNameApiReq.getUserName(),changeUserNameApiReq.getNewUserName());
    }

    @PutMapping("/changeName")
    public String changeName(@RequestBody ChangeNameApiReq changeNameApiReq){
        return userService.changeName(changeNameApiReq.getUserName(),changeNameApiReq.getNewName());
    }

    @PutMapping("/follow/{currentUser}")
    public String followUser(@RequestParam("userToBeFollowed") String userToBeFollowed,@PathVariable String currentUser ){

        return userService.addFollower(userToBeFollowed,currentUser);
    }

    @GetMapping("/allusers")
    public GetAllUserApiRes getAllUser(){
        return new GetAllUserApiRes(userService.getAllUsers());
    }

    @PutMapping("/unfollow/{currUser}")
    public String unfollowUser(@RequestParam("userToBeUnfollowed") String userToBeUnfollowed,@PathVariable String currUser){
        return userService.removeFollower(userToBeUnfollowed,currUser);
    }

    @GetMapping("/getAllFollowers/{userName}")
    public List<String> getAllFollowers(@PathVariable String userName){
        return userService.getAllFollowers(userName);
    }

    @GetMapping("/getAllFollowing/{userName}")
    public List<String> getAllFollowing(@PathVariable String userName){
        return userService.getAllFollowing(userName);
    }

    @PutMapping("/blockuser/{userName}")
    public String block(@PathVariable String userName,@RequestParam String userToBeBlocked){
        return userService.block(userName,userToBeBlocked);
    }

    @PutMapping("/unBlockuser/{userName}")
    public String unBlock(@PathVariable String userName,@RequestParam String userToBeUnBlocked){
        return userService.block(userName,userToBeUnBlocked);
    }


}
