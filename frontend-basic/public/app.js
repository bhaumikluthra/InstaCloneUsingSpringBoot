// API Configuration
const API_BASE_URL = 'http://localhost:8080';

// State Management
let currentUser = JSON.parse(localStorage.getItem('user')) || null;

// DOM Elements
const authContainer = document.getElementById('auth-container');
const appContainer = document.getElementById('app-container');
const registerForm = document.getElementById('register-form');
const loginForm = document.getElementById('login-form');
const postForm = document.getElementById('post-form');
const bioForm = document.getElementById('bio-form');
const postsContainer = document.getElementById('posts-container');
const userPosts = document.getElementById('user-posts');

// Initialize App
document.addEventListener('DOMContentLoaded', () => {
    setupEventListeners();
    checkAuthState();
});

// Event Listeners Setup
function setupEventListeners() {
    // Tab Navigation
    document.querySelectorAll('.tab').forEach(tab => {
        tab.addEventListener('click', () => {
            document.querySelectorAll('.tab, .form').forEach(el => el.classList.remove('active'));
            tab.classList.add('active');
            document.querySelector(`.form#${tab.dataset.tab}-form`).classList.add('active');
        });
    });

    // Auth Forms
    registerForm.addEventListener('submit', handleRegister);
    loginForm.addEventListener('submit', handleLogin);
    
    // Navigation
    document.getElementById('feed-btn').addEventListener('click', () => showSection('feed'));
    document.getElementById('profile-btn').addEventListener('click', () => showSection('profile'));
    document.getElementById('logout-btn').addEventListener('click', handleLogout);
    
    // Post Form
    postForm.addEventListener('submit', handleCreatePost);
    
    // Bio Form
    bioForm.addEventListener('submit', handleUpdateBio);
}

// Authentication Functions
async function handleRegister(e) {
    e.preventDefault();
    try {
        const data = {
            userName: document.getElementById('reg-username').value,
            name: document.getElementById('reg-name').value,
            password: document.getElementById('reg-password').value
        };
        
        console.log('Attempting to register with data:', data);
        const response = await apiRequest('/adduser', 'POST', data);
        console.log('Registration response:', response);
        showToast('Registration successful!', 'success');
        loginUser(data);
    } catch (error) {
        console.error('Registration error:', error);
        showToast(error.message || 'Failed to register. Please try again.', 'error');
    }
}

async function handleLogin(e) {
    e.preventDefault();
    try {
        const data = {
            userName: document.getElementById('login-username').value,
            password: document.getElementById('login-password').value
        };

        // Since there's no login endpoint, we'll verify against all users
        const users = await apiRequest('/allusers', 'GET');
        const user = users.find(u => u.userName === data.userName);
        
        if (user) {
            loginUser(user);
        } else {
            throw new Error('Invalid credentials');
        }
    } catch (error) {
        showToast(error.message, 'error');
    }
}

function loginUser(user) {
    currentUser = user;
    localStorage.setItem('user', JSON.stringify(user));
    checkAuthState();
    showSection('feed');
}

function handleLogout() {
    currentUser = null;
    localStorage.removeItem('user');
    checkAuthState();
}

// Post Functions
async function handleCreatePost(e) {
    e.preventDefault();
    try {
        const data = {
            postTitle: document.getElementById('post-title').value,
            content: document.getElementById('post-content').value,
        };

        await apiRequest('/uploadPost', 'POST', data);
        showToast('Post created successfully!', 'success');
        loadPosts();
        e.target.reset();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function loadPosts() {
    try {
        const posts = await apiRequest(`/allpost/${currentUser.userName}`, 'GET');
        renderPosts(posts, postsContainer);
    } catch (error) {
        showToast('Failed to load posts', 'error');
    }
}

async function handleUpdateBio(e) {
    e.preventDefault();
    try {
        const bio = document.getElementById('user-bio').value;
        await apiRequest('/addBio', 'POST', {
            userName: currentUser.userName,
            bio: bio
        });
        showToast('Bio updated successfully!', 'success');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// UI Functions
function checkAuthState() {
    if (currentUser) {
        authContainer.classList.add('hidden');
        appContainer.classList.remove('hidden');
        loadUserProfile();
        loadPosts();
    } else {
        authContainer.classList.remove('hidden');
        appContainer.classList.add('hidden');
    }
}

function showSection(section) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.getElementById(`${section}-section`).classList.add('active');
    
    document.querySelectorAll('.nav-btn').forEach(btn => btn.classList.remove('active'));
    document.getElementById(`${section}-btn`).classList.add('active');
    
    if (section === 'profile') {
        loadUserProfile();
    } else if (section === 'feed') {
        loadPosts();
    }
}

async function loadUserProfile() {
    if (!currentUser) return;
    
    try {
        // Get user details
        document.getElementById('profile-name').textContent = currentUser.name;
        document.getElementById('profile-username').textContent = `@${currentUser.userName}`;
        
        // Load stats
        const followers = await apiRequest(`/getAllFollowers/${currentUser.userName}`, 'GET');
        const following = await apiRequest(`/getAllFollowing/${currentUser.userName}`, 'GET');
        const posts = await apiRequest(`/allpost/${currentUser.userName}`, 'GET');
        
        document.getElementById('posts-count').textContent = posts.length;
        document.getElementById('followers-count').textContent = followers.length;
        document.getElementById('following-count').textContent = following.length;
        
        // Render user's posts
        renderPosts(posts, userPosts);
    } catch (error) {
        showToast('Failed to load profile', 'error');
    }
}

function renderPosts(posts, container) {
    if (!posts.length) {
        container.innerHTML = '<p class="no-posts">No posts yet</p>';
        return;
    }

    container.innerHTML = posts.map(post => `
        <div class="post-card">
            <h3>${post.postTitle}</h3>
            <p>${post.content}</p>
        </div>
    `).join('');
}

// Utility Functions
async function apiRequest(endpoint, method = 'GET', data = null) {
    try {
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors' // Allow CORS requests
        };

        if (data) {
            options.body = JSON.stringify(data);
        }

        console.log('Making request:', `${API_BASE_URL}${endpoint}`, options);
        const response = await fetch(`${API_BASE_URL}${endpoint}`, options);
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || `Request failed with status ${response.status}`);
        }

        const responseText = await response.text();
        try {
            return responseText ? JSON.parse(responseText) : null;
        } catch {
            return responseText;
        }
    } catch (error) {
        console.error('API Request failed:', error);
        throw error;
    }
}

function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type}`;
    toast.style.display = 'block';
    
    setTimeout(() => {
        toast.style.display = 'none';
    }, 3000);
}