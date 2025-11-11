//package com.instaApp.instaClone.entity;
//
//
//public class Post {
//    private static  int postId=0;
//    private final String postTitle;
//    private final String content;
//    public Post(String postTitle, String content ) {
//        postId++;
//        this.postTitle=postTitle;
//        this.content=content;
//    }
//    public String getPostTitle() {
//        return postTitle;
//    }
//    public String getContent() {
//        return content;
//    }
//    public  int getPostId() {
//        return postId;
//    }
//}

package com.instaApp.instaClone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*; // Make sure to use jakarta.persistence imports
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    // Renamed for clarity, often called 'title' or 'caption'
    private String postTitle;

    @Column(columnDefinition = "TEXT") // Use TEXT for potentially long content
    private String content;

    /**
     * This defines the many-to-one relationship.
     * Many Posts can belong to one User.
     * The @JoinColumn creates a foreign key column named 'user_username' in the 'posts' table.
     */

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_username")
    private User user;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    // A no-argument constructor is required by JPA
    public Post() {
    }

    // You can keep a constructor for convenience
    public Post(String postTitle, String content) {
        this.postTitle = postTitle;
        this.content = content;

    }


    // Standard Getters and Setters for all fields
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


}