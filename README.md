# InstaClone: A Full-Stack Social Media Application

This project is a full-stack web application inspired by Instagram, developed to demonstrate modern software development practices. It features a robust backend built with Spring Boot and a dynamic, responsive frontend built with React. The application supports core social media functionalities, including user authentication, profiles, posts, and social interactions.

---

## Technologies Used

| Category      | Technology                                    |
|---------------|-----------------------------------------------|
| **Backend** | Java, Spring Boot, Spring Data JPA, Spring Security |
| **Frontend** | React, Vite, JavaScript, HTML5, CSS3          |
| **Database** | MySQL (Development), PostgreSQL (Deployment)  |
| **Build Tools** | Apache Maven                                  |
| **DevOps** | Git, GitHub, Heroku (Backend), Netlify (Frontend) |

---

## Core Features

The application currently supports the following features:

* **User Authentication:** Secure user registration with password hashing (BCrypt) and a functional login system.
* **User Profiles:** Dynamic routing to view individual user profiles, displaying their name and username.
* **Social Graph:** Functionality for users to follow and unfollow each other. Follower and following counts are displayed on user profiles.
* **User Discovery:** A live search bar to find other users on the platform.
* **Post Creation:** Logged-in users can create and publish new posts with a title and content, which appear on their profile.
* **Post Viewing:** All posts made by a user are displayed on their profile page.

---

## Roadmap: Planned Future Features

The following functionalities are already supported by the backend API and are planned for implementation in the frontend UI, along with other enhancements.

### User Profile Management
* **Edit Profile:** Allowing users to update their profile information, such as their name and bio.
* **Delete Profile:** Implementing the functionality for a user to permanently delete their own account and associated data.

### Advanced User Interactions
* **Block & Unblock:** A user interface for the existing block/unblock feature. When a user is blocked, all social connections (following/followers) between the two users will be severed.
* **View Block List:** A private page for a user to see the list of accounts they have blocked.

### Post Management
* **Edit Post:** Functionality for users to edit the title and content of their own posts.
* **Delete Post:** A UI element allowing users to delete their posts.

### Core Social Features
* **Main Feed:** A home page that displays a feed of posts from the users that the current user is following.
* **Liking Posts:** The ability for users to "like" a post.
* **Commenting on Posts:** A system for users to leave comments on posts.

---

## Local Setup and Installation

To run this project locally, please follow these steps:

### Prerequisites
* Java JDK 21 or newer
* Node.js and npm
* A running MySQL instance

### Backend Setup
1.  Navigate to the `Backend` directory: `cd Backend`
2.  Configure your MySQL database credentials in `src/main/resources/application.properties`.
3.  Run the application: `./mvnw spring-boot:run`
4.  The backend server will start on `http://localhost:8080`.

### Frontend Setup
1.  Navigate to the `Frontend` directory: `cd Frontend`
2.  Install dependencies: `npm install`
3.  Run the development server: `npm run dev`
4.  The frontend will be available at `http://localhost:5173`.
