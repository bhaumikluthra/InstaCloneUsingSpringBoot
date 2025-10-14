// import { useState, useEffect } from 'react';
// import { useParams } from 'react-router-dom';
// import { useAuth } from '../context/AuthContext';
// import Post from '../components/Post';
// import CreatePostForm from '../components/CreatePostForm';
//
// export default function ProfilePage() {
//     const { username } = useParams();
//     const { currentUser } = useAuth();
//     const currentUsername = currentUser?.username;
//
//     const [profileUser, setProfileUser] = useState(null);
//     const [posts, setPosts] = useState([]);
//     const [followers, setFollowers] = useState([]);
//     const [following, setFollowing] = useState([]);
//
//
//     const [isFollowing, setIsFollowing] = useState(false);
//
//     // Fetch profile data
//     const fetchProfileData = async () => {
//         try {
//             const userRes = await fetch(`http://localhost:8080/api/users/${username}`);
//             const userData = await userRes.json();
//             setProfileUser(userData);
//
//             const postsRes = await fetch(`http://localhost:8080/api/users/${username}/posts`);
//             const postsData = await postsRes.json();
//             setPosts(postsData);
//
//             const followersRes = await fetch(`http://localhost:8080/api/users/${username}/followers`);
//             const followersData = await followersRes.json();
//             setFollowers(followersData);
//
//             const followingRes = await fetch(`http://localhost:8080/api/users/${username}/following`);
//             const followingData = await followingRes.json();
//             setFollowing(followingData);
//
//             // ✅ FIXED: check correct property of follower object
//             // Log once to confirm the structure
//             console.log("Followers data:", followersData);
//
//             // Try all possible key names (adjust if needed based on console)
//             if (Array.isArray(followersData)) {
//                 const isUserFollowing = followersData.some(follower => follower.userName === currentUsername);
//
//                 setIsFollowing(isUserFollowing);
//             } else {
//                 setIsFollowing(false);
//             }
//         } catch (error) {
//             console.error("Failed to fetch profile data", error);
//         }
//     };
//
//     useEffect(() => {
//         if (username) {
//             fetchProfileData();
//         }
//     }, [username, currentUsername]);
//
//     const handleFollow = async () => {
//         if (!currentUsername) return;
//         const res = await fetch(`http://localhost:8080/api/users/${currentUsername}/follow/${username}`, { method: 'POST' });
//         if (res.ok) {
//             setIsFollowing(true);
//             // Optimistically update followers array
//             setFollowers(prev => [...prev, { userName: currentUsername }]);
//         }
//     };
//
//     const handleUnfollow = async () => {
//         if (!currentUsername) return;
//         const res = await fetch(`http://localhost:8080/api/users/${currentUsername}/follow/${username}`, { method: 'DELETE' });
//         if (res.ok) {
//             setIsFollowing(false);
//             // Remove current user from followers array
//             setFollowers(prev => prev.filter(f => f.userName !== currentUsername));
//         }
//     };
//
//
//
//     if (!profileUser) return <p>Loading profile...</p>;
//
//     return (
//         <div className="container">
//             <header className="profile-header card">
//                 <h2>{profileUser.name}</h2>
//                 <p className="username">@{profileUser.username}</p>
//                 <div className="profile-stats">
//                     <span><strong>{posts.length}</strong> posts</span>
//                     <span><strong>{followers.length}</strong> followers</span>
//                     <span><strong>{following.length}</strong> following</span>
//                 </div>
//
//                 {/* Follow/Unfollow button only for other users */}
//                 {currentUsername !== username && (
//                     <div className="profile-actions">
//                         {isFollowing ? (
//                             <button onClick={handleUnfollow}>Unfollow</button>
//                         ) : (
//                             <button onClick={handleFollow}>Follow</button>
//                         )}
//                     </div>
//                 )}
//             </header>
//
//             <div className="profile-content">
//                 {currentUsername === username && (
//                     <CreatePostForm onPostCreated={fetchProfileData} />
//                 )}
//
//                 <div className="posts-grid">
//                     <h3>Posts</h3>
//                     {posts.length > 0 ? (
//                         posts.map((post) => (
//                             <Post
//                                 key={post.postId}
//                                 post={post}
//                                 currentUsername={currentUsername}
//                                 onPostUpdated={fetchProfileData}
//                             />
//                         ))
//                     ) : (
//                         <p>{username} hasn't posted anything yet.</p>
//                     )}
//                 </div>
//             </div>
//         </div>
//     );
// }
//
//
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Post from '../components/Post';
import CreatePostForm from '../components/CreatePostForm';

export default function ProfilePage() {
    const { username } = useParams();
    const { currentUser } = useAuth();
    const currentUsername = currentUser?.username;

    const [profileUser, setProfileUser] = useState(null);
    const [posts, setPosts] = useState([]);
    const [followers, setFollowers] = useState([]);
    const [following, setFollowing] = useState([]);

    const [isFollowing, setIsFollowing] = useState(false);

    const [bio, setBio] = useState("");
    const [editingBio, setEditingBio] = useState(false);

    // Modal state
    const [showFollowersModal, setShowFollowersModal] = useState(false);
    const [showFollowingModal, setShowFollowingModal] = useState(false);

    const fetchProfileData = async () => {
        try {
            const userRes = await fetch(`http://localhost:8080/api/users/${username}`);
            const userData = await userRes.json();
            setProfileUser(userData);
            setBio(userData.bio || ""); // set bio from backend

            const postsRes = await fetch(`http://localhost:8080/api/users/${username}/posts`);
            const postsData = await postsRes.json();
            setPosts(postsData);

            const followersRes = await fetch(`http://localhost:8080/api/users/${username}/followers`);
            const followersData = await followersRes.json();
            setFollowers(followersData);

            const followingRes = await fetch(`http://localhost:8080/api/users/${username}/following`);
            const followingData = await followingRes.json();
            setFollowing(followingData);

            const isUserFollowing = Array.isArray(followersData)
                ? followersData.some(f => f.userName === currentUsername)
                : false;
            setIsFollowing(isUserFollowing);

        } catch (error) {
            console.error("Failed to fetch profile data", error);
        }
    };

    useEffect(() => {
        if (username) fetchProfileData();
    }, [username, currentUsername]);

    const handleFollow = async () => {
        if (!currentUsername) return;
        const res = await fetch(
            `http://localhost:8080/api/users/${currentUsername}/follow/${username}`,
            { method: 'POST' }
        );
        if (res.ok) {
            setIsFollowing(true);
            setFollowers(prev => [...prev, { userName: currentUsername }]);
        }
    };

    const handleUnfollow = async () => {
        if (!currentUsername) return;
        const res = await fetch(
            `http://localhost:8080/api/users/${currentUsername}/follow/${username}`,
            { method: 'DELETE' }
        );
        if (res.ok) {
            setIsFollowing(false);
            setFollowers(prev => prev.filter(f => f.userName !== currentUsername));
        }
    };

    const handleBioUpdate = async () => {
        try {
            const res = await fetch(`http://localhost:8080/api/users/${currentUsername}/bio`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ bio }),
            });
            if (res.ok) {
                const updatedUser = await res.json();
                setProfileUser(updatedUser);
                setBio(updatedUser.bio);
                setEditingBio(false);
            }
        } catch (err) {
            console.error("Error updating bio", err);
        }
    };

    if (!profileUser) return <p>Loading profile...</p>;

    return (
        <div className="container">
            <header className="profile-header card">
                <h2>{profileUser.name}</h2>
                <p className="username">@{profileUser.username}</p>

                {/* Bio Section */}
                {currentUsername === username ? (
                    editingBio ? (
                        <div>
                            <input
                                type="text"
                                value={bio}
                                onChange={(e) => setBio(e.target.value)}
                                placeholder="Enter your bio"
                            />
                            <button onClick={handleBioUpdate}>Save Bio</button>
                            <button onClick={() => setEditingBio(false)}>Cancel</button>
                        </div>
                    ) : (
                        <div>
                            <p>{bio || "No bio yet."}</p>
                            <button onClick={() => setEditingBio(true)}>
                                {bio ? "Update Bio" : "Add Bio"}
                            </button>
                        </div>
                    )
                ) : (
                    <p>{bio || ""}</p>
                )}

                <div className="profile-stats">
                    <span><strong>{posts.length}</strong> posts</span>
                    <span
                        style={{ cursor: 'pointer' }}
                        onClick={() => setShowFollowersModal(true)}
                    >
                        <strong>{followers.length}</strong> followers
                    </span>
                    <span
                        style={{ cursor: 'pointer' }}
                        onClick={() => setShowFollowingModal(true)}
                    >
                        <strong>{following.length}</strong> following
                    </span>
                </div>

                {currentUsername !== username && (
                    <div className="profile-actions">
                        {isFollowing ? (
                            <button onClick={handleUnfollow}>Unfollow</button>
                        ) : (
                            <button onClick={handleFollow}>Follow</button>
                        )}
                    </div>
                )}
            </header>

            <div className="profile-content">
                {currentUsername === username && (
                    <CreatePostForm onPostCreated={fetchProfileData} />
                )}

                <div className="posts-grid">
                    <h3>Posts</h3>
                    {posts.length > 0 ? (
                        posts.map((post) => (
                            <Post
                                key={post.postId}
                                post={post}
                                currentUsername={currentUsername}
                                onPostUpdated={fetchProfileData}
                            />
                        ))
                    ) : (
                        <p>{username} hasn't posted anything yet.</p>
                    )}
                </div>
            </div>

            {/* Followers Modal */}
            {showFollowersModal && (
                <div className="modal">
                    <div className="modal-content">
                        <h3>Followers</h3>
                        <button className="close-btn" onClick={() => setShowFollowersModal(false)}>X</button>
                        <ul>
                            {followers.length > 0 ? followers.map(f => (
                                <li key={f.userName}>{f.userName}</li>
                            )) : <li>No followers yet</li>}
                        </ul>
                    </div>
                </div>
            )}

            {/* Following Modal */}
            {showFollowingModal && (
                <div className="modal">
                    <div className="modal-content">
                        <h3>Following</h3>
                        <button className="close-btn" onClick={() => setShowFollowingModal(false)}>X</button>
                        <ul>
                            {following.length > 0 ? following.map(f => (
                                <li key={f.userName}>{f.userName}</li>
                            )) : <li>Not following anyone</li>}
                        </ul>
                    </div>
                </div>
            )}
        </div>
    );
}
