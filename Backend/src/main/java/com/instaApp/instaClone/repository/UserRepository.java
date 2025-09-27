//package com.instaApp.instaClone.repository;
//
//import com.instaApp.instaClone.dataStore.UserDataStore;
//import com.instaApp.instaClone.entity.Post;
//import com.instaApp.instaClone.entity.User;
//import com.instaApp.instaClone.service.dto.UserDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Repository
//public class UserRepository {
//    @Autowired
//    private UserDataStore userdataStore;
//    @Autowired
//    private UserDataStore userDataStore;
//
//    public String addUser(UserDto user){
//
//        User user1=new User(user.getUserName(),user.getName(),user.getPassword());
//        if(userdataStore.getUsername(user1.getUsername())==null){
//            userdataStore.addUser(user1);
//        }
//        else{
//            return "user already exists with this username";
//        }
//
//        return "user added with user id:"+user1.getUsername();
//    }
//
//    public String addBio(String bio,String userName){
//        User user1=userdataStore.getUsername(userName);
//
//        if(user1!=null){
//            user1.addBio(bio);
//            userdataStore.updateUser(user1);
//            return user1.getBio();
//        }
//        return "User does not exist with username";
//    }
//
//    public String deleteBio(String userName){
//
//        User user=userdataStore.getUsername(userName);
//        if(user!=null){
//
//            user.deleteBio();
//            userdataStore.updateUser(user);
//            return "your bio has been deleted";
//        }
//        return "User does not exist with username";
//
//    }
//
//    public String changeUserName(String currUserName,String newUserName){
//        if(currUserName==null || newUserName==null){return "invalid username";}
//        else if(userdataStore.getUsername(currUserName)==null){
//            return "User does not exist with username";
//        }
//        else if(currUserName.equals(newUserName)){
//            return "no changes in new username";
//        }
//        else{
//            if(userdataStore.getUsername(newUserName)==null){
//                User user=userdataStore.getUsername(currUserName);
//                user.changeUserName(newUserName);
//                userdataStore.updateUser(user);
//                userdataStore.removeUser(currUserName);
//                return "username has been changed to "+user.getUsername();
//            }else{
//                return "user already exists with username: "+newUserName;
//            }
//        }
//    }
//
//    public String changeName(String UserName,String name){
//        if(userdataStore.getUsername(UserName)==null){
//            return "User does not exist with this username";
//        }
//
//        else if(name==null)return "Name cannot be empty";
//
//        else if(userdataStore.getName(UserName).equals(name)){
//            return "Name is same as before";
//        }
//
//        else{
//          User user=userdataStore.getUsername(UserName);
//          user.changeName(name);
//          return "Name has been changed to "+user.getName();
//        }
//    }
//
//    public List<String> getFollowerFollowers(String myUserName,String followerUserName){
//        User user=userdataStore.getUsername(myUserName);
//        User user1=userdataStore.getUsername(followerUserName);
//        if(user.getFollowing().contains(followerUserName)){
//            return user1.getFollowers();
//        }
//        return user.getFollowing();
//    }
//
//    public String changeBio(String bio,String userName){
//
//        User user=userdataStore.getUsername(userName);
//        if(user!=null){
//
//            user.addBio(bio);
//            userdataStore.updateUser(user);
//            return "your Bio has been changed: "+user.getBio();
//        }
//        return "user does not exist";
//    }
//
//    public String addStatus(String userName,String status){
//        if(userdataStore.getUsername(userName)==null){
//            return "User does not exist with this username";
//        }
//        User user=userdataStore.getUsername(userName);
//        user.addStatus(status);
//        userdataStore.updateUser(user);
//        return "status has been updated to:"+status;
//    }
//
//    public String deleteStatus(String userName){
//        if(userdataStore.getUsername(userName)==null){
//            return "User does not exist with this username";
//        }
//        User user=userdataStore.getUsername(userName);
//        user.deleteStatus();
//        userdataStore.updateUser(user);
//        return "status has been deleted";
//    }
//
//    public String block(String userName, String userToBeBlocked){
//        if(userdataStore.getUsername(userName)==null){
//            return "User does not exist with this username";
//        }
//        if(userdataStore.getUsername(userToBeBlocked)==null){
//            return "User does not exist with the username you want to block";
//        }
//        else if(userName.equals(userToBeBlocked)){
//            return "You cannot block yourself";
//        }
//        else if(userdataStore.getUsername(userToBeBlocked).getBlockedUsers().contains(userName)){
//            return "user unavailable";
//        }
//
//        User user=userdataStore.getUsername(userName);
//        User user1=userdataStore.getUsername(userToBeBlocked);
//        user.blockuser(userToBeBlocked);
//        if(user.getFollowing().contains(userToBeBlocked)){
//            user.removeFollowing(userToBeBlocked);
//            user1.removeFollower(userName);
//        }
//        if(user.getFollowers().contains(userToBeBlocked)){
//            user.removeFollower(userToBeBlocked);
//            user1.removeFollowing(userName);
//        }
//        userdataStore.updateUser(user);
//        return "user has been blocked with userName: "+userToBeBlocked;
//    }
//
//    public String unBlockUser(String userName,String userToBeUnblocked){
//        if(userdataStore.getUsername(userName)==null){
//            return "User does not exist with this username";
//        }
//        if(userdataStore.getUsername(userToBeUnblocked)==null){
//            return "User does not exist with the username you want to unblock";
//        }
//        if(!userdataStore.getUsername(userName).getBlockedUsers().contains(userToBeUnblocked)){
//            return "You cannot unblock someone you have never blocked";
//        }
//        User user=userdataStore.getUsername(userName);
//        user.unBlockUser(userToBeUnblocked);
//        userdataStore.updateUser(user);
//        return "user has been unblocked with userName: "+userToBeUnblocked;
//    }
//
//    public String followUser(String tbf,String ci){
//
//        User usertbf=userdataStore.getUsername(tbf);
//        User userci=userdataStore.getUsername(ci);
//        if(usertbf.getBlockedUsers().contains(userci)){
//            return "this profile is not available";
//        }
//        if(userci.getBlockedUsers().contains(usertbf)){
//            return "you have blocked this user";
//        }
//        usertbf.addFollower(ci);
//        userci.addFollowing(tbf);
//        userdataStore.updateUser(usertbf);
//        userdataStore.updateUser(userci);
//        return "user with user id:"+ci+" with name "+userci.getUsername()+" followed user with user id:"+tbf+" with name "+usertbf.getUsername();
//    }
//
//    public String unfollowUser(String tbuf,String ci){
//        User usertbuf=userdataStore.getUsername(tbuf);
//        User userci=userdataStore.getUsername(ci);
//        usertbuf.removeFollower(ci);
//        userci.removeFollowing(tbuf);
//        userdataStore.updateUser(usertbuf);
//        userdataStore.updateUser(userci);
//        return "user with with username "+userci.getUsername()+" followed user with username "+usertbuf.getUsername();
//    }
//
//    public List<String> getAllFollowers(String Username)
//    {
//        User curruser=userdataStore.getUsername(Username);
//        return curruser.getFollowers();
//    }
//
//    public List<String> getAllFollowing(String Username)
//    {
//        User curruser=userdataStore.getUsername(Username);
//        return curruser.getFollowing();
//    }
//
//    public List<UserDto> getAllUsers(){
//        List<User>ls=userdataStore.getAllUser();
//        List<UserDto>ls1=new ArrayList<>();
//        for(int i=0;i<ls.size();i++){
//            ls1.add(new UserDto(ls.get(i).getUsername(),ls.get(i).getName(),ls.get(i).getBio(),ls.get(i).getStatus(),ls.get(i).getPassword()));
//        }
//        return ls1;
//    }
//
//}


package com.instaApp.instaClone.repository;

import com.instaApp.instaClone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Add this import

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // ADD THIS METHOD:
    // This will find all users whose username contains the search query, ignoring case.
    List<User> findByUsernameContainingIgnoreCase(String username);
}