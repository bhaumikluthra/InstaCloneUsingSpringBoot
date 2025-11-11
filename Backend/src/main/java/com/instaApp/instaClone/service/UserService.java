
package com.instaApp.instaClone.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instaApp.instaClone.entity.User;
import com.instaApp.instaClone.repository.UserRepository;
import com.instaApp.instaClone.service.dto.UserDto;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(UserDto userDto) {
        if (userRepository.existsById(userDto.getUserName())) {
            throw new RuntimeException("User already exists with username: " + userDto.getUserName());
        }
        User userEntity = new User();
        userEntity.setUsername(userDto.getUserName());
        userEntity.setName(userDto.getName());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userEntity);
    }

    public User loginUser(UserDto userDto) {
        User user = findByUsername(userDto.getUserName());
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public User findByUsername(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }

    public User updateBio(String username, String bio) {
        User user = findByUsername(username);
        user.setBio(bio);
        return userRepository.save(user);
    }

    @Transactional
    public String followUser(String followerUsername, String usernameToFollow) {
        User follower = findByUsername(followerUsername);
        User userToFollow = findByUsername(usernameToFollow);
        follower.getFollowing().add(userToFollow);
        userRepository.save(follower);
        return followerUsername + " is now following " + usernameToFollow;
    }

    @Transactional
    public String unfollowUser(String followerUsername, String usernameToUnfollow) {
        User follower = findByUsername(followerUsername);
        User userToUnfollow = findByUsername(usernameToUnfollow);
        follower.getFollowing().remove(userToUnfollow);
        userRepository.save(follower);
        return followerUsername + " has unfollowed " + usernameToUnfollow;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public String blockUser(String username, String usernameToBlock) {
        if (username.equals(usernameToBlock)) {
            throw new IllegalArgumentException("You cannot block yourself");
        }
        User currentuser = findByUsername(username);
        User userToBlock = findByUsername(usernameToBlock);
        if (currentuser.getFollowing().contains(userToBlock)) {
            currentuser.getFollowing().remove(userToBlock);
            userToBlock.getFollowers().remove(currentuser);
        }
        if (userToBlock.getFollowing().contains(currentuser)) {
            userToBlock.getFollowing().remove(currentuser);
            currentuser.getFollowers().remove(userToBlock);
            userRepository.save(userToBlock);
        }
        currentuser.getBlockedUsers().add(userToBlock);
        userRepository.save(currentuser);
        return usernameToBlock + " is now blocked";
    }

    @Transactional
    public String unblockUser(String username, String usernameToUnblock) {
        if (username.equals(usernameToUnblock)) {
            throw new IllegalArgumentException("You cannot unblock yourself");
        }
        User currentuser = findByUsername(username);
        User userToUnblock = findByUsername(usernameToUnblock);
        if (currentuser.getBlockedUsers().contains(userToUnblock)) {
            currentuser.getBlockedUsers().remove(userToUnblock);
            userRepository.save(currentuser);
            return usernameToUnblock + " has been unblocked";
        }
        return usernameToUnblock + " was not blocked";
    }

    public List<String> getBlockedUsernames(String username) {
        User user = findByUsername(username);
        Set<User> blockedUsers = user.getBlockedUsers();
        return blockedUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public List<UserDto> getBlockedUsers(String username) {
        User user = findByUsername(username);
        Set<User> blockedUsers = user.getBlockedUsers();
        return blockedUsers.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<String> getUsersWhoBlocked(String username) {
        return userRepository.findAll().stream()
                .filter(u -> u.getBlockedUsers().stream().anyMatch(b -> b.getUsername().equals(username)))
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public List<UserDto> getFollowers(String username) {
        User user = findByUsername(username);
        return user.getFollowers().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getFollowersFiltered(String username, String requester) {
        List<UserDto> followers = getFollowers(username);
        if (requester == null) return followers;
        if (requester.equals(username)) return followers;
        List<String> usersWhoBlockedRequester = getUsersWhoBlocked(requester);
        return followers.stream()
                .filter(u -> !usersWhoBlockedRequester.contains(u.getUserName()) || u.getUserName().equals(requester))
                .collect(Collectors.toList());
    }

    public List<UserDto> getFollowing(String username) {
        User user = findByUsername(username);
        return user.getFollowing().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getFollowingFiltered(String username, String requester) {
        List<UserDto> following = getFollowing(username);
        if (requester == null) return following;
        if (requester.equals(username)) return following;
        List<String> usersWhoBlockedRequester = getUsersWhoBlocked(requester);
        return following.stream()
                .filter(u -> !usersWhoBlockedRequester.contains(u.getUserName()) || u.getUserName().equals(requester))
                .collect(Collectors.toList());
    }

    private UserDto mapEntityToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserName(user.getUsername());
        dto.setName(user.getName());
        dto.setBio(user.getBio());
        dto.setStatus(user.getStatus());
        return dto;
    }

}