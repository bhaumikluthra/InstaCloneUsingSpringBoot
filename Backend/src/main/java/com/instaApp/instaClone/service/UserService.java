//package com.instaApp.instaClone.service;
//
//
//import com.instaApp.instaClone.repository.UserRepository;
//import com.instaApp.instaClone.service.dto.UserDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import java.util.List;
//
//@Service
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//
//    public String addUser(UserDto user){
//        return userRepository.addUser(user);
//    }
//
//    public String addBio(String bio,String userName){
//        return userRepository.addBio(bio,userName);
//    }
//
//    public String deleteBio(String userName){
//        return userRepository.deleteBio(userName);
//    }
//
//    public String changeBio(String bio,String userName){
//        return userRepository.changeBio(bio,userName);
//    }
//
//    public String addStatus(String userName,String status){
//        return userRepository.addStatus(userName,status);
//    }
//
//    public String deleteStatus(String userName){
//        return userRepository.deleteStatus(userName);
//    }
//
//
//
//    public String changeUserName(String currUsername,String newUserName){
//        return userRepository.changeUserName(currUsername,newUserName);
//    }
//
//    public String changeName(String userName,String name){
//        return userRepository.changeName(userName,name);
//    }
//
//    public String addFollower(String tbf,String ci){
//        return userRepository.followUser(tbf, ci);
//    }
//
//    public String removeFollower(String tbuf,String ci){
//        return userRepository.unfollowUser(tbuf, ci);
//    }
//
//    public String block(String userName,String userToBeBlocked){
//        return userRepository.block(userName,userToBeBlocked);
//    }
//
//    public String unblock(String userName,String userToBeUnBlocked){
//        return userRepository.unBlockUser(userName,userToBeUnBlocked);
//    }
//
//
//
//    public List<String> getAllFollowers(String userName){
//        return userRepository.getAllFollowers(userName);
//    }
//
//    public  List<String> getAllFollowing(String userName){
//        return userRepository.getAllFollowing(userName);
//    }
//
//    public List<UserDto> getAllUsers(){
//        return userRepository.getAllUsers();
//    }
//
//
//
//
//
//}
package com.instaApp.instaClone.service;

import com.instaApp.instaClone.entity.User;
import com.instaApp.instaClone.repository.UserRepository;
import com.instaApp.instaClone.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // A password encoder to securely hash passwords
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Creates a new user with a hashed password.
     */
    public User createUser(UserDto userDto) {
        if (userRepository.existsById(userDto.getUserName())) {
            throw new RuntimeException("User already exists with username: " + userDto.getUserName());
        }
        User userEntity = new User();
        userEntity.setUsername(userDto.getUserName());
        userEntity.setName(userDto.getName());

        // Hash the user's password before saving it to the database
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(userEntity);
    }

    /**
     * Authenticates a user by checking their password.
     */
    public User loginUser(UserDto userDto) {
        User user = findByUsername(userDto.getUserName());

        // Securely compare the submitted password with the hashed password from the database
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return user; // Passwords match, login is successful
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    /**
     * Finds a user by their username. A helper method for other services.
     */
    public User findByUsername(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }

    /**
     * Updates a user's bio.
     */
    public User updateBio(String username, String bio) {
        User user = findByUsername(username);
        user.setBio(bio);
        return userRepository.save(user);
    }

    /**
     * Allows one user to follow another.
     */
    @Transactional
    public String followUser(String followerUsername, String usernameToFollow) {
        User follower = findByUsername(followerUsername);
        User userToFollow = findByUsername(usernameToFollow);
        follower.getFollowing().add(userToFollow);
        userRepository.save(follower);
        return followerUsername + " is now following " + usernameToFollow;
    }

    /**
     * Allows one user to unfollow another.
     */
    @Transactional
    public String unfollowUser(String followerUsername, String usernameToUnfollow) {
        User follower = findByUsername(followerUsername);
        User userToUnfollow = findByUsername(usernameToUnfollow);
        follower.getFollowing().remove(userToUnfollow);
        userRepository.save(follower);
        return followerUsername + " has unfollowed " + usernameToUnfollow;
    }

    /**
     * Retrieves all users.
     */
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<String> getBlockedUsernames(String username) {
        User user = findByUsername(username);
        // Corrected method call
        Set<User> blockedUsers = user.getBlockedUsers();

        return blockedUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }


    /**
     * Retrieves a user's followers as a list of DTOs.
     */
    public List<UserDto> getFollowers(String username) {
        User user = findByUsername(username);
        // Map the Set<User> of followers to a List<UserDto>
        return user.getFollowers().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the users someone is following as a list of DTOs.
     */
    public List<UserDto> getFollowing(String username) {
        User user = findByUsername(username);
        return user.getFollowing().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * A helper method to map a User entity to a UserDto.
     */
    private UserDto mapEntityToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserName(user.getUsername());
        dto.setName(user.getName());
        dto.setBio(user.getBio());
        dto.setStatus(user.getStatus());
        // IMPORTANT: Never expose the password in a DTO
        return dto;
    }
}