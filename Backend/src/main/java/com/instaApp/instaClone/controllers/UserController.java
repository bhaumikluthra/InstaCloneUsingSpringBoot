//package com.instaApp.instaClone.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.instaApp.instaClone.controllers.models.AddBioApiReq;
//import com.instaApp.instaClone.controllers.models.AddStatusApiReq;
//import com.instaApp.instaClone.controllers.models.ChangeNameApiReq;
//import com.instaApp.instaClone.controllers.models.ChangeUserNameApiReq;
//import com.instaApp.instaClone.controllers.models.GetAllUserApiRes;
//import com.instaApp.instaClone.controllers.models.RegisterUserApiReq;
//import com.instaApp.instaClone.service.UserService;
//import com.instaApp.instaClone.service.dto.UserDto;
//
//import jakarta.validation.Valid;
//
//@RestController
//@CrossOrigin(origins = "http://localhost:3000")
//public class UserController {
//    @Autowired
//    UserService userService;
//
//    @PostMapping("/adduser")
//    public String registerUser(@RequestBody @Valid RegisterUserApiReq registerUserApiReq, BindingResult bindingResult) {
//        if(bindingResult.hasErrors()) {
//            return "username not valid";
//        }
//        UserDto userDto = new UserDto(registerUserApiReq.getUserName(),registerUserApiReq.getName(),registerUserApiReq.getPassword());
//        return userService.addUser(userDto);
//    }
//
//    @PostMapping("/addBio")
//    public String addBio(@RequestBody AddBioApiReq addBioApiReq){
//        return userService.addBio(addBioApiReq.getBio(),addBioApiReq.getUserName());
//    }
//
//    @DeleteMapping("/deleteBio/{userName}")
//    public String deleteBio(@PathVariable String userName){
//        return userService.deleteBio(userName);
//    }
//
//    @PutMapping("/changeBio")
//    public String changeBio(@RequestBody AddBioApiReq addBioApiReq){
//        return userService.changeBio(addBioApiReq.getBio(),addBioApiReq.getUserName());
//    }
//
//    @PostMapping("/addStatus")
//    public String addStatus(@RequestBody AddStatusApiReq addStatusApiReq){
//        return userService.addStatus(addStatusApiReq.getUserName(),addStatusApiReq.getStatus());
//    }
//
//    @DeleteMapping("/deleteStatus/{userName}")
//    public String deleteStatus(@PathVariable String userName){
//        return userService.deleteStatus(userName);
//    }
//
//
//    @PutMapping("/changeUserName")
//    public String changeUserName(@RequestBody ChangeUserNameApiReq changeUserNameApiReq){
//        return userService.changeUserName(changeUserNameApiReq.getUserName(),changeUserNameApiReq.getNewUserName());
//    }
//
//    @PutMapping("/changeName")
//    public String changeName(@RequestBody ChangeNameApiReq changeNameApiReq){
//        return userService.changeName(changeNameApiReq.getUserName(),changeNameApiReq.getNewName());
//    }
//
//    @PutMapping("/follow/{currentUser}")
//    public String followUser(@RequestParam("userToBeFollowed") String userToBeFollowed,@PathVariable String currentUser ){
//
//        return userService.addFollower(userToBeFollowed,currentUser);
//    }
//
//    @GetMapping("/allusers")
//    public GetAllUserApiRes getAllUser(){
//        return new GetAllUserApiRes(userService.getAllUsers());
//    }
//
//    @PutMapping("/unfollow/{currUser}")
//    public String unfollowUser(@RequestParam("userToBeUnfollowed") String userToBeUnfollowed,@PathVariable String currUser){
//        return userService.removeFollower(userToBeUnfollowed,currUser);
//    }
//
//    @GetMapping("/getAllFollowers/{userName}")
//    public List<String> getAllFollowers(@PathVariable String userName){
//        return userService.getAllFollowers(userName);
//    }
//
//    @GetMapping("/getAllFollowing/{userName}")
//    public List<String> getAllFollowing(@PathVariable String userName){
//        return userService.getAllFollowing(userName);
//    }
//
//    @PutMapping("/blockuser/{userName}")
//    public String block(@PathVariable String userName,@RequestParam String userToBeBlocked){
//        return userService.block(userName,userToBeBlocked);
//    }
//
//    @PutMapping("/unBlockuser/{userName}")
//    public String unBlock(@PathVariable String userName,@RequestParam String userToBeUnBlocked){
//        return userService.block(userName,userToBeUnBlocked);
//    }
//
//
//}

package com.instaApp.instaClone.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // <-- Make sure to import this
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.instaApp.instaClone.entity.User;
import com.instaApp.instaClone.service.UserService;
import com.instaApp.instaClone.service.dto.UserDto;


// ... other imports




@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registers a new user.
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            // This is the "happy path" - it runs when the user is created successfully.
            User createdUser = userService.createUser(userDto);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            // This block only runs if an error is thrown (e.g., user already exists).
            // It catches the error and returns the message with a 409 Conflict status.
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
    @PostMapping("/login") //
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.loginUser(userDto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Gets a user by their username.
     * GET /api/users/{username}
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user); // Returns the user with a 200 OK status
    }

    /**
     * Gets all users.
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Updates a user's bio.
     * PUT /api/users/{username}/bio
     */
    @PutMapping("/{username}/bio")
    public ResponseEntity<User> updateUserBio(@PathVariable String username, @RequestBody Map<String, String> payload) {
        String bio = payload.get("bio");
        User updatedUser = userService.updateBio(username, bio);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Makes one user follow another.
     * POST /api/users/{followerUsername}/follow/{usernameToFollow}
     */
    @PostMapping("/{followerUsername}/follow/{usernameToFollow}")
    public ResponseEntity<String> followUser(@PathVariable String followerUsername, @PathVariable String usernameToFollow) {
        String result = userService.followUser(followerUsername, usernameToFollow);
        return ResponseEntity.ok(result);
    }

    /**
     * Makes one user unfollow another.
     * DELETE /api/users/{followerUsername}/follow/{usernameToUnfollow}
     */
    @DeleteMapping("/{followerUsername}/follow/{usernameToUnfollow}")
    public ResponseEntity<String> unfollowUser(@PathVariable String followerUsername, @PathVariable String usernameToUnfollow) {
        String result = userService.unfollowUser(followerUsername, usernameToUnfollow);
        return ResponseEntity.ok(result);
    }

    
    @GetMapping("/{username}/following")
    public ResponseEntity<List<UserDto>> getFollowing(@PathVariable String username,
                                                     @RequestParam(required = false) String requester) {
        List<UserDto> following = (requester == null)
                ? userService.getFollowing(username)
                : userService.getFollowingFiltered(username, requester);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserDto>> getFollowers(@PathVariable String username,
                                                    @RequestParam(required = false) String requester) {
        List<UserDto> followers = (requester == null)
                ? userService.getFollowers(username)
                : userService.getFollowersFiltered(username, requester);
        return ResponseEntity.ok(followers);
    }

    @PutMapping("/{username}/block/{usernameToBeBlocked}")
    public ResponseEntity<String> blockUser(@PathVariable String username, @PathVariable String usernameToBeBlocked) {
        String result= userService.blockUser(username, usernameToBeBlocked);
        return ResponseEntity.ok(result);
    }

    /**
     * Unblock endpoint: DELETE /api/users/{username}/block/{usernameToBeUnblocked}
     */
    @DeleteMapping("/{username}/block/{usernameToBeUnblocked}")
    public ResponseEntity<String> unblockUser(@PathVariable String username, @PathVariable String usernameToBeUnblocked) {
        String result = userService.unblockUser(username, usernameToBeUnblocked);
        return ResponseEntity.ok(result);
    }

    /**
     * Get list of usernames that the given user has blocked.
     * GET /api/users/{username}/blocked
     */
    @GetMapping("/{username}/blocked")
    public ResponseEntity<List<String>> getBlocked(@PathVariable String username) {
        List<String> blocked = userService.getBlockedUsernames(username);
        return ResponseEntity.ok(blocked);
    }

        /**
         * Get list of full UserDto objects that the given user has blocked.
         * GET /api/users/{username}/blockedUsers
         */
        @GetMapping("/{username}/blockedUsers")
        public ResponseEntity<List<UserDto>> getBlockedUsers(@PathVariable String username) {
            List<UserDto> blockedUsers = userService.getBlockedUsers(username);
            return ResponseEntity.ok(blockedUsers);
        }
    /**
     * Get list of usernames who have blocked the given user.
     * GET /api/users/{username}/blockedBy
     */
    @GetMapping("/{username}/blockedBy")
    public ResponseEntity<List<String>> getBlockedBy(@PathVariable String username) {
        List<String> blockedBy = userService.getUsersWhoBlocked(username);
        return ResponseEntity.ok(blockedBy);
    }

}