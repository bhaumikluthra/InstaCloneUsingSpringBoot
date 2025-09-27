import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Post from '../components/Post'; // Import the Post component
import CreatePostForm from '../components/CreatePostForm'; // Import the form component

export default function ProfilePage() {
    const { username } = useParams();
    const { currentUser } = useAuth();
    const [profileUser, setProfileUser] = useState(null);
    const [posts, setPosts] = useState([]); // State for the user's posts
    const [followers, setFollowers] = useState([]);
    const [following, setFollowing] = useState([]);
    const [isFollowing, setIsFollowing] = useState(false);

    const fetchProfileData = async () => {
        try {
            const userRes = await fetch(`http://localhost:8080/api/users/${username}`);
            const userData = await userRes.json();
            setProfileUser(userData);

            const postsRes = await fetch(`http://localhost:8080/api/users/${username}/posts`);
            const postsData = await postsRes.json();
            setPosts(postsData);

            const followersRes = await fetch(`http://localhost:8080/api/users/${username}/followers`);
            const followersData = await followersRes.json();
            setFollowers(followersData);

            const followingRes = await fetch(`http://localhost:8080/api/users/${username}/following`);
            const followingData = await followingRes.json();
            setFollowing(followingData);

            setIsFollowing(followersData.some(follower => follower.userName === currentUser));
        } catch (error) {
            console.error("Failed to fetch profile data", error);
        }
    };

    useEffect(() => {
        if (username) {
            fetchProfileData();
        }
    }, [username, currentUser]);

    const handleFollow = async () => {
        await fetch(`http://localhost:8080/api/users/${currentUser}/follow/${username}`, { method: 'POST' });
        fetchProfileData(); // Refresh all profile data
    };

    const handleUnfollow = async () => {
        await fetch(`http://localhost:8080/api/users/${currentUser}/follow/${username}`, { method: 'DELETE' });
        fetchProfileData(); // Refresh all profile data
    };

    if (!profileUser) return <p>Loading profile...</p>;

    return (
        <div className="container">
            <header className="profile-header card">
                <h2>{profileUser.name}</h2>
                <p className="username">@{profileUser.username}</p>
                <div className="profile-stats">
                    <span><strong>{posts.length}</strong> posts</span>
                    <span><strong>{followers.length}</strong> followers</span>
                    <span><strong>{following.length}</strong> following</span>
                </div>
                {currentUser !== username && (
                    <div className="profile-actions">
                        {isFollowing ?
                            <button onClick={handleUnfollow}>Unfollow</button> :
                            <button onClick={handleFollow}>Follow</button>
                        }
                    </div>
                )}
            </header>

            <div className="profile-content">
                {/* Only show the create post form if this is the logged-in user's profile */}
                {currentUser === username && (
                    <CreatePostForm onPostCreated={fetchProfileData} />
                )}

                <div className="posts-grid">
                    <h3>Posts</h3>
                    {posts.length > 0 ? (
                        posts.map(post => <Post key={post.postId} post={post} />)
                    ) : (
                        <p>{username} hasn't posted anything yet.</p>
                    )}
                </div>
            </div>
        </div>
    );
}