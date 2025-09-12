package com.instaApp.instaClone.entity;
import java.util.*;

public class User
{

    private  String name;
    private  String username;
    private String password;
    private int age;

    private String bio;
    private String status;

    private List<String>following=new ArrayList<>();
    private List<String>followers=new ArrayList<>();
    private List<String>blocked=new ArrayList<>();

    public User(String username,String name,int age,String status,String password){
        this.username=username;
        this.name=name;
        this.age=age;
        this.password=password;
    }



    public void addBio(String bio){
        this.bio=bio;
    }

    public void deleteBio(){
        this.bio=null;
    }

    public void addStatus(String status){
        this.status=status;
    }

    public String getPassword(){
        return password;
    }

    public void blockuser(String username){
        blocked.add(username);
    }

    public void deleteStatus(){
        this.status=null;
    }

    public String getStatus(){
        return status;
    }

    public void changeUserName(String newUserName){
        this.username=newUserName;
    }

    public void changeName(String newName){
        this.name=newName;
    }

    public String getUsername() {
        return username;
    }

    public  String getName() {
        return name;
    }

    public void addFollower(String userName){
        followers.add(userName);
    }

    public String getBio() {

        if(bio!=null && !bio.isEmpty()){
            return bio;
        }
        return "you do not have a bio";
    }

    public void addFollowing(String userName){
        following.add(userName);
    }

    public void removeFollower(String userName){
        followers.remove(userName);
    }

    public void removeFollowing(String userName){
        following.remove(userName);
    }

    public List<String> getFollowers(){
        return followers;
    }

    public List<String> getFollowing(){
        return following;
    }

    public void blockUser(String userToBeBlocked){
        blocked.add(userToBeBlocked);
    }
    public void unBlockUser(String userToBeUnBlocked){
        blocked.remove(userToBeUnBlocked);
    }
    public List<String> getBlockedUsers(){
        return blocked;
    }




}
