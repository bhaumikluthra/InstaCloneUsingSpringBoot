// DOM Elements and Utilities
const $ = (s, p = document) => p.querySelector(s);
const $$ = (s, p = document) => Array.from(p.querySelectorAll(s));
const toastBox = $('.toast');
const tabs = $$('.nav-btn');
const panels = $$('.panel');
const themeBtn = $('.theme');
const sidebarToggle = $('.brand');

// Toast notifications
function showToast(message, type = '') {
  if (!toastBox) return;
  const t = document.createElement('div');
  t.className = `t ${type}`;
  t.textContent = message;
  toastBox.appendChild(t);
  
  // Add slide-in animation
  setTimeout(() => {
    t.classList.add('show');
  }, 10);
  
  // Remove after delay with fade-out
  setTimeout(() => {
    t.style.opacity = '0';
    t.style.transform = 'translateY(10px)';
    setTimeout(() => t.remove(), 300);
  }, 3000);
}

// Tab navigation
tabs.forEach(tab => {
  tab.addEventListener('click', () => {
    tabs.forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    
    const target = tab.dataset.target;
    panels.forEach(p => p.classList.remove('active'));
    document.getElementById(target).classList.add('active');
    
    // Close sidebar on mobile when tab is clicked
    if (window.innerWidth <= 1000) {
      $('.sidebar').classList.remove('active');
    }
  });
});

// Sidebar toggle for mobile
if (sidebarToggle) {
  sidebarToggle.addEventListener('click', (e) => {
    if (window.innerWidth <= 1000) {
      $('.sidebar').classList.toggle('active');
    }
  });
}

// Theme toggle
if (themeBtn) {
  themeBtn.addEventListener('click', () => {
    document.body.classList.toggle('light');
    
    // Update icon
    const icon = themeBtn.querySelector('i');
    if (document.body.classList.contains('light')) {
      icon.className = 'fas fa-moon';
    } else {
      icon.className = 'fas fa-sun';
    }
  });
}

// HTTP helpers
async function http(url, method = 'GET', data = null) {
  try {
    // Show loading spinner in the button if it's a form submission
    let submitBtn;
    let originalText;
    if (method !== 'GET') {
      submitBtn = document.activeElement;
      if (submitBtn && submitBtn.tagName === 'BUTTON') {
        originalText = submitBtn.innerHTML;
        submitBtn.innerHTML = '<span class="spinner"></span> Processing...';
        submitBtn.disabled = true;
      }
    }
    
    const options = {
      method,
      headers: { 'Content-Type': 'application/json' }
    };
    
    if (data) options.body = JSON.stringify(data);
    
    const response = await fetch(url, options);
    const text = await response.text();
    let result;
    try {
      result = JSON.parse(text);
    } catch {
      result = { message: text };
    }
    
    // Restore button state
    if (submitBtn) {
      setTimeout(() => {
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
      }, 500); // Small delay for better UX
    }
    
    if (!response.ok) throw new Error(result.error || result.message || 'Request failed');
    return result;
  } catch (err) {
    showToast(err.message, 'err');
    throw err;
  }
}

// User registration
const registerForm = $('#register-form');
if (registerForm) {
  registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = $('#username').value;
    const name = $('#name').value;
    
    // Basic validation
    if (!username || !name) {
      showToast('Please fill in all fields', 'err');
      return;
    }
    
    try {
      await http('/adduser', 'POST', { userName: username, name });
      showToast('User registered successfully!', 'ok');
      registerForm.reset();
      loadUsers();
      
      // Switch to Users tab after successful registration
      setTimeout(() => {
        $('[data-target="users-panel"]').click();
      }, 1000);
    } catch (err) {
      console.error('Registration error:', err);
    }
  });
}

// Load and display users
async function loadUsers() {
  try {
    const usersList = $('#users-list');
    if (!usersList) return;
    
    usersList.innerHTML = '<div class="spinner" style="margin: 20px auto; display: block;"></div>';
    
    const result = await http('/allusers', 'GET');
    const users = result?.users || result || [];
    usersList.innerHTML = '';
    
    if (users.length === 0) {
      usersList.innerHTML = '<div class="empty-state">No users found. Be the first to register!</div>';
      return;
    }
    
    users.forEach(user => {
      const username = user.userName || user;
      const name = user.name || '';
      
      const li = document.createElement('li');
      
      const userInfo = document.createElement('div');
      userInfo.className = 'user-info';
      userInfo.innerHTML = `
        <div class="avatar-small">
          <img src="https://ui-avatars.com/api/?name=${encodeURIComponent(name || username)}&background=3b82f6&color=fff" alt="${username}">
        </div>
        <div>
          <strong>@${username}</strong>
          <div class="muted">${name}</div>
        </div>
      `;
      
      const actions = document.createElement('div');
      actions.className = 'button-group';
      
      const followBtn = document.createElement('button');
      followBtn.className = 'btn-primary';
      followBtn.innerHTML = '<i class="fas fa-user-plus"></i> Follow';
      followBtn.addEventListener('click', () => followUser(username));
      
      const blockBtn = document.createElement('button');
      blockBtn.className = 'btn-secondary';
      blockBtn.innerHTML = '<i class="fas fa-ban"></i> Block';
      blockBtn.addEventListener('click', () => blockUser(username));
      
      actions.appendChild(followBtn);
      actions.appendChild(blockBtn);
      
      li.appendChild(userInfo);
      li.appendChild(actions);
      usersList.appendChild(li);
    });
    
    showToast('Users loaded successfully', 'ok');
  } catch (err) {
    console.error('Error loading users:', err);
    const usersList = $('#users-list');
    if (usersList) {
      usersList.innerHTML = '<div class="error-state">Failed to load users. Please try again later.</div>';
    }
  }
}

// Profile management
const profileForm = $('#profile-form');
if (profileForm) {
  profileForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = $('#profile-username').value;
    const newName = $('#profile-name').value;
    
    if (!username || !newName) {
      showToast('Please fill in all fields', 'err');
      return;
    }
    
    try {
      await http('/changename', 'POST', { userName: username, newName });
      showToast('Name updated successfully!', 'ok');
      
      // Update profile display
      const profileName = $('.profile-name');
      if (profileName) profileName.textContent = newName;
      
      // Update avatar if using name-based avatar
      const avatarImg = $('.profile-avatar img');
      if (avatarImg && avatarImg.src.includes('ui-avatars')) {
        avatarImg.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(newName)}&background=3b82f6&color=fff`;
      }
    } catch (err) {
      console.error('Error updating name:', err);
    }
  });
}

// Bio management
const bioForm = $('#bio-form');
if (bioForm) {
  bioForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = $('#bio-username').value;
    const bio = $('#bio-text').value;
    
    if (!username) {
      showToast('Username is required', 'err');
      return;
    }
    
    try {
      if (bio) {
        await http('/addbio', 'POST', { userName: username, bio });
        showToast('Bio added successfully!', 'ok');
        
        // Update bio display
        const profileBio = $('.profile-bio');
        if (profileBio) {
          profileBio.textContent = bio;
          profileBio.style.display = 'block';
        }
      } else {
        await http('/deletebio', 'POST', { userName: username });
        showToast('Bio removed successfully!', 'ok');
        
        // Update bio display
        const profileBio = $('.profile-bio');
        if (profileBio) {
          profileBio.textContent = 'No bio added yet';
          profileBio.style.display = 'none';
        }
      }
    } catch (err) {
      console.error('Error updating bio:', err);
    }
  });
}

// Profile picture upload simulation
const profilePicBtn = $('.btn-circle');
if (profilePicBtn) {
  profilePicBtn.addEventListener('click', () => {
    // In a real app, this would open a file picker
    // For demo purposes, we'll just change to a random avatar
    const colors = ['3b82f6', '10b981', 'f59e0b', 'ef4444', '8b5cf6'];
    const randomColor = colors[Math.floor(Math.random() * colors.length)];
    const username = $('#profile-username').value;
    const name = $('.profile-name')?.textContent || '';
    
    const profileAvatar = $('.profile-avatar img');
    if (profileAvatar) {
      profileAvatar.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(name || username)}&background=${randomColor}&color=fff`;
    }
    
    showToast('Profile picture updated!', 'ok');
  });
}

// Follow/unfollow/block user
async function followUser(username) {
  try {
    const currentUser = $('#inp-current-user')?.value || 'demo_user';
    await http(`/follow/${encodeURIComponent(currentUser)}?userToBeFollowed=${encodeURIComponent(username)}`, 'PUT');
    showToast(`You are now following ${username}`, 'ok');
    
    // Update UI to show following status
    const userItem = Array.from($$('#users-list li')).find(
      li => li.querySelector('strong').textContent === `@${username}`
    );
    
    if (userItem) {
      const followBtn = userItem.querySelector('.btn-primary');
      if (followBtn) {
        followBtn.innerHTML = '<i class="fas fa-user-check"></i> Following';
        followBtn.classList.remove('btn-primary');
        followBtn.classList.add('btn-secondary');
        followBtn.onclick = () => unfollowUser(username);
      }
      
      // Add followed pill
      const userInfo = userItem.querySelector('.user-info');
      if (userInfo && !userInfo.querySelector('.pill')) {
        const pill = document.createElement('span');
        pill.className = 'pill';
        pill.innerHTML = '<i class="fas fa-check"></i> Following';
        userInfo.appendChild(pill);
      }
    }
    
    // Update follower count in profile if on profile page
    const followerCount = $('.follower-count');
    if (followerCount) {
      followerCount.textContent = parseInt(followerCount.textContent || '0') + 1;
    }
  } catch (err) {
    console.error('Error following user:', err);
  }
}

async function unfollowUser(username) {
  try {
    const currentUser = $('#inp-current-user')?.value || 'demo_user';
    await http(`/unfollow/${encodeURIComponent(currentUser)}?userToBeUnfollowed=${encodeURIComponent(username)}`, 'PUT');
    showToast(`You have unfollowed ${username}`, 'ok');
    
    // Update UI to show not following status
    const userItem = Array.from($$('#users-list li')).find(
      li => li.querySelector('strong').textContent === `@${username}`
    );
    
    if (userItem) {
      const followBtn = userItem.querySelector('button');
      if (followBtn) {
        followBtn.innerHTML = '<i class="fas fa-user-plus"></i> Follow';
        followBtn.classList.remove('btn-secondary');
        followBtn.classList.add('btn-primary');
        followBtn.onclick = () => followUser(username);
      }
      
      // Remove followed pill
      const pill = userItem.querySelector('.pill');
      if (pill) pill.remove();
    }
    
    // Update follower count in profile if on profile page
    const followerCount = $('.follower-count');
    if (followerCount) {
      const count = parseInt(followerCount.textContent || '0');
      followerCount.textContent = Math.max(0, count - 1);
    }
  } catch (err) {
    console.error('Error unfollowing user:', err);
  }
}

async function blockUser(username) {
  // Ask for confirmation
  if (!confirm(`Are you sure you want to block ${username}? You won't see their posts and they won't see yours.`)) {
    return;
  }
  
  try {
    const currentUser = $('#inp-current-user')?.value || 'demo_user';
    await http(`/blockuser/${encodeURIComponent(currentUser)}?userToBeBlocked=${encodeURIComponent(username)}`, 'PUT');
    showToast(`You have blocked ${username}`, 'ok');
    
    // Update UI to show blocked status
    const userItem = Array.from($$('#users-list li')).find(
      li => li.querySelector('strong').textContent === `@${username}`
    );
    
    if (userItem) {
      // Add blocked styling
      userItem.classList.add('blocked-user');
      
      // Update buttons
      const actions = userItem.querySelector('.button-group');
      if (actions) {
        actions.innerHTML = `
          <button class="btn-danger">
            <i class="fas fa-ban"></i> Blocked
          </button>
          <button class="btn-secondary unblock-btn">
            <i class="fas fa-unlock"></i> Unblock
          </button>
        `;
        
        // Add unblock functionality
        const unblockBtn = actions.querySelector('.unblock-btn');
        if (unblockBtn) {
          unblockBtn.addEventListener('click', () => unblockUser(username));
        }
      }
    }
  } catch (err) {
    console.error('Error blocking user:', err);
  }
}

async function unblockUser(username) {
  try {
    const currentUser = $('#inp-current-user')?.value || 'demo_user';
    await http(`/unBlockuser/${encodeURIComponent(currentUser)}?userToBeUnBlocked=${encodeURIComponent(username)}`, 'PUT');
    showToast(`You have unblocked ${username}`, 'ok');
    
    // Refresh the users list to show updated status
    loadUsers();
  } catch (err) {
    console.error('Error unblocking user:', err);
  }
}

// Post management
const postForm = $('#post-form');
if (postForm) {
  const postTextarea = $('#post-content');
  const charCounter = $('.char-counter');
  const MAX_CHARS = 280;
  
  if (postTextarea && charCounter) {
    postTextarea.addEventListener('input', () => {
      const remaining = MAX_CHARS - postTextarea.value.length;
      charCounter.textContent = `${remaining} characters remaining`;
      
      if (remaining < 0) {
        charCounter.style.color = 'var(--danger)';
        $('#publish-btn').disabled = true;
      } else {
        charCounter.style.color = 'var(--muted)';
        $('#publish-btn').disabled = false;
      }
    });
  }
  
  postForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = $('#post-username').value;
    const title = $('#post-title')?.value || '';
    const content = $('#post-content').value;
    
    if (!username || !content) {
      showToast('Please fill in all required fields', 'err');
      return;
    }
    
    if (content.length > MAX_CHARS) {
      showToast('Post exceeds maximum character limit', 'err');
      return;
    }
    
    try {
      await http('/uploadPost', 'POST', { 
        userName: username, 
        postTitle: title,
        postContent: content 
      });
      showToast('Post published successfully!', 'ok');
      postForm.reset();
      
      // Reset character counter
      if (charCounter) {
        charCounter.textContent = `${MAX_CHARS} characters remaining`;
        charCounter.style.color = 'var(--muted)';
      }
      
      loadPosts();
    } catch (err) {
      console.error('Error publishing post:', err);
    }
  });
}

async function loadPosts() {
  try {
    const postsList = $('#posts-list');
    if (!postsList) return;
    
    postsList.innerHTML = '<div class="spinner" style="margin: 20px auto; display: block;"></div>';
    
    const username = $('#inp-post-user')?.value || 'all';
    const result = await http(`/allpost/${encodeURIComponent(username)}`, 'GET');
    const posts = result?.posts || result || [];
    
    postsList.innerHTML = '';
    
    if (posts.length === 0) {
      postsList.innerHTML = '<div class="empty-state">No posts yet. Be the first to share something!</div>';
      return;
    }
    
    posts.forEach(post => {
      const li = document.createElement('li');
      
      // Format date
      const postDate = post.timestamp ? new Date(post.timestamp) : new Date();
      const formattedDate = postDate.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
      
      const postInfo = document.createElement('div');
      postInfo.className = 'post-content';
      postInfo.innerHTML = `
        <div class="post-header">
          <div class="avatar-small">
            <img src="https://ui-avatars.com/api/?name=${encodeURIComponent(post.userName || 'user')}&background=3b82f6&color=fff" alt="${post.userName || 'user'}">
          </div>
          <div>
            <strong>@${post.userName || 'user'}</strong>
            <div class="muted">${formattedDate}</div>
          </div>
        </div>
        ${post.postTitle ? `<h3>${post.postTitle}</h3>` : ''}
        <div class="post-body">${post.postContent || ''}</div>
      `;
      
      const actions = document.createElement('div');
      actions.className = 'post-actions';
      
      // Like button (for demonstration)
      const likeBtn = document.createElement('button');
      likeBtn.className = 'btn-icon';
      likeBtn.innerHTML = '<i class="far fa-heart"></i>';
      likeBtn.title = 'Like';
      likeBtn.addEventListener('click', function() {
        this.innerHTML = '<i class="fas fa-heart" style="color: var(--danger);"></i>';
        showToast('Post liked!', 'ok');
      });
      
      // Comment button (for demonstration)
      const commentBtn = document.createElement('button');
      commentBtn.className = 'btn-icon';
      commentBtn.innerHTML = '<i class="far fa-comment"></i>';
      commentBtn.title = 'Comment';
      commentBtn.addEventListener('click', () => {
        showToast('Comments feature coming soon!', 'ok');
      });
      
      actions.appendChild(likeBtn);
      actions.appendChild(commentBtn);
      
      // Delete button (only for user's own posts)
      const currentUser = $('#inp-post-user')?.value;
      if (currentUser && post.userName === currentUser) {
        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'btn-icon';
        deleteBtn.innerHTML = '<i class="far fa-trash-alt"></i>';
        deleteBtn.title = 'Delete';
        deleteBtn.addEventListener('click', () => deletePost(post.id, username));
        actions.appendChild(deleteBtn);
      }
      
      li.appendChild(postInfo);
      li.appendChild(actions);
      postsList.appendChild(li);
    });
    
    showToast('Posts loaded successfully', 'ok');
  } catch (err) {
    console.error('Error loading posts:', err);
    const postsList = $('#posts-list');
    if (postsList) {
      postsList.innerHTML = '<div class="error-state">Failed to load posts. Please try again later.</div>';
    }
  }
}

async function deletePost(postId, username) {
  // Ask for confirmation
  if (!confirm('Are you sure you want to delete this post? This action cannot be undone.')) {
    return;
  }
  
  try {
    const user = username || $('#inp-post-user').value;
    await http(`/delete/${encodeURIComponent(user)}?postId=${encodeURIComponent(postId)}`, 'DELETE');
    showToast('Post deleted successfully!', 'ok');
    loadPosts();
  } catch (err) {
    console.error('Error deleting post:', err);
  }
}

// Empty state and error state styles
const style = document.createElement('style');
style.textContent = `
  .empty-state, .error-state {
    padding: 40px 20px;
    text-align: center;
    background: var(--elev);
    border-radius: 12px;
    border: 1px dashed var(--border);
    color: var(--muted);
  }
  
  .error-state {
    border-color: var(--danger-light);
    color: var(--danger-light);
  }
  
  .avatar-small {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    overflow: hidden;
    border: 2px solid var(--brand-light);
  }
  
  .avatar-small img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 12px;
  }
  
  .post-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
  }
  
  .post-body {
    margin-bottom: 16px;
    line-height: 1.6;
    white-space: pre-wrap;
  }
  
  .post-content {
    flex: 1;
  }
  
  .blocked-user {
    opacity: 0.7;
    position: relative;
  }
  
  .blocked-user::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(239, 68, 68, 0.05);
    pointer-events: none;
    border-radius: 12px;
  }
  
  .toast .t {
    transform: translateY(10px);
    opacity: 0;
    transition: all 0.3s ease;
  }
  
  .toast .t.show {
    transform: translateY(0);
    opacity: 1;
  }
`;
document.head.appendChild(style);

// Initialize
document.addEventListener('DOMContentLoaded', () => {
  // Set first tab as active
  if (tabs.length > 0) tabs[0].click();
  
  // Set theme icon based on current theme
  if (themeBtn) {
    const icon = document.createElement('i');
    icon.className = 'fas fa-sun';
    themeBtn.appendChild(icon);
  }
  
  // Initialize profile avatar if on profile page
  const profileAvatar = $('.profile-avatar img');
  if (profileAvatar) {
    const username = $('#profile-username').value;
    const name = $('#profile-name').value;
    profileAvatar.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(name || username)}&background=3b82f6&color=fff`;
  }
  
  // Load initial data
  loadUsers();
  loadPosts();
});

// Add window resize listener for responsive sidebar
window.addEventListener('resize', () => {
  if (window.innerWidth > 1000) {
    $('.sidebar').classList.remove('active');
  }
});


