//package com.instaApp.instaClone.entity;
//import java.util.*;
//
//public class User
//{
//
//    private  String name;
//    private  String username;
//    private String password;
//
//
//    private int age;
//
//    private String bio;
//    private String status;
//
//    private List<String>following=new ArrayList<>();
//    private List<String>followers=new ArrayList<>();
//    private List<String>blocked=new ArrayList<>();
//
//    public User(String username,String name,String password){
//        this.username=username;
//        this.name=name;
//        this.password=password;
//    }
//
//
//    public void addBio(String bio){
//        this.bio=bio;
//    }
//
//    public void deleteBio(){
//        this.bio=null;
//    }
//
//    public void addStatus(String status){
//        this.status=status;
//    }
//
//
//
//    public void blockuser(String username){
//        blocked.add(username);
//    }
//
//    public void deleteStatus(){
//        this.status=null;
//    }
//
//    public String getStatus(){
//        return status;
//    }
//
//    public String getPassword(){
//        return password;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//    public void changeUserName(String newUserName){
//        this.username=newUserName;
//    }
//
//    public void changeName(String newName){
//        this.name=newName;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public  String getName() {
//        return name;
//    }
//
//    public void addFollower(String userName){
//        followers.add(userName);
//    }
//
//    public String getBio() {
//
//        if(bio!=null && !bio.isEmpty()){
//            return bio;
//        }
//        return "you do not have a bio";
//    }
//
//    public void addFollowing(String userName){
//        following.add(userName);
//    }
//
//    public void removeFollower(String userName){
//        followers.remove(userName);
//    }
//
//    public void removeFollowing(String userName){
//        following.remove(userName);
//    }
//
//    public List<String> getFollowers(){
//        return followers;
//    }
//
//    public List<String> getFollowing(){
//        return following;
//    }
//
//    public void blockUser(String userToBeBlocked){
//        blocked.add(userToBeBlocked);
//    }
//    public void unBlockUser(String userToBeUnBlocked){
//        blocked.remove(userToBeUnBlocked);
//    }
//    public List<String> getBlockedUsers(){
//        return blocked;
//    }
//
//
//
//
//}


package com.instaApp.instaClone.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String username;

    private String name;

    private String password;

    private int age;

    private String bio;

    private String status;


    @JsonManagedReference
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Post> posts = new ArrayList<>();

    /**
     * Represents the set of users that this user is following.
     * A join table named 'user_following' will be created to manage this relationship.
     */
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "user_following",
            joinColumns = @JoinColumn(name = "user_id"), // Column for the current user
            inverseJoinColumns = @JoinColumn(name = "following_id") // Column for the user being followed
    )
    private Set<User> following = new HashSet<>();

    /**
     * Represents the set of users that follow this user.
     * This is the inverse side of the 'following' relationship.
     * 'mappedBy' tells Hibernate that the 'user_following' table is already defined by the 'following' field.
     */
    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

//Represents the set of users that this user has blocked.
    @ManyToMany
    @JoinTable(
            name = "user_blocked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blocked_id")
    )
    private Set<User> blocked = new HashSet<>();

    // JPA requires a no-argument constructor
    public User() {
    }

    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Set<User> getFollowing() { return following; }
    public void setFollowing(Set<User> following) { this.following = following; }
    public Set<User> getFollowers() { return followers; }
    public void setFollowers(Set<User> followers) { this.followers = followers; }
    public Set<User> getBlockedUsers() { return blocked; }
    public void setBlocked(Set<User> blocked) { this.blocked = blocked; }
    public List<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}