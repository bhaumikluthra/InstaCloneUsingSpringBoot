# InstaClone: A Full-Stack Social Media Application

This project is a full-stack web application inspired by Instagram, developed to demonstrate modern software development practices. It features a robust backend built with Spring Boot and a dynamic, responsive frontend built with React.

---

## Project Status

* **Status:** In Development
* **Backend:** The backend API is feature-complete, providing all necessary endpoints for user management, posts, and social interactions.
* **Frontend:** The React frontend is currently under active development to implement the user interface for all backend features.
* **Deployment:** This application is intended for local development and is not yet deployed.

---

## Technologies Used

| Category      | Technology                                    |
|---------------|-----------------------------------------------|
| **Backend** | Java, Spring Boot, Spring Data JPA, Spring Security |
| **Frontend** | React, Vite, JavaScript, HTML5, CSS3          |
| **Database** | MySQL                                         |
| **Build Tools** | Apache Maven                                  |
| **Tools** | Git, GitHub                                   |

---

## Features

### Backend API (Complete)
The backend provides a complete RESTful API with the following capabilities:
- Secure user registration with BCrypt password hashing.
- User login and authentication.
- Full CRUD (Create, Read, Update, Delete) operations for User Profiles.
- Post creation and viewing.
- A social graph system supporting Follow/Unfollow and Block/Unblock functionality.
- A dynamic user search endpoint.
- An endpoint to generate a personalized feed for a user based on who they follow.

### Frontend UI (In Progress)
The React frontend currently implements:
- User registration and secure login pages.
- A multi-page experience using React Router.
- A home page with a user search feature.
- Dynamic profile pages that display user information and posts.
- Functional "Follow" and "Unfollow" buttons on user profiles.

---

## Frontend Roadmap

The following features are supported by the backend and are the next priorities for implementation in the React UI:

- **Main Feed:** Develop the home page to display a chronological feed of posts from users that the current user follows.
- **Profile Management:** Implement UI for users to edit their bio and other profile details.
- **Block/Unblock UI:** Add a "Block" button to user profiles and a settings page to manage blocked users.
- **Post Management:** Create UI for users to edit and delete their own posts.
- **Social Features:** Implement functionality for liking and commenting on posts.

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
