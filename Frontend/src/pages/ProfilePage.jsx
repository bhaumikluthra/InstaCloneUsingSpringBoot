
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

    // Block lists
    const [blockedByMe, setBlockedByMe] = useState([]); // users I have blocked
    const [blockedMe, setBlockedMe] = useState([]); // users who have blocked me
    const [blockedUsers, setBlockedUsers] = useState([]); // detailed blocked user objects (for profile owner only)
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

            const followersUrl = currentUsername
                ? `http://localhost:8080/api/users/${username}/followers?requester=${encodeURIComponent(currentUsername)}`
                : `http://localhost:8080/api/users/${username}/followers`;
            const followersRes = await fetch(followersUrl);
            const followersData = await followersRes.json();
            setFollowers(followersData);

            const followingUrl = currentUsername
                ? `http://localhost:8080/api/users/${username}/following?requester=${encodeURIComponent(currentUsername)}`
                : `http://localhost:8080/api/users/${username}/following`;
            const followingRes = await fetch(followingUrl);
            const followingData = await followingRes.json();
            setFollowing(followingData);

            // fetch block lists for current user (who I blocked and who blocked me)
            if (currentUsername) {
                try {
                    const blockedByMeRes = await fetch(`http://localhost:8080/api/users/${currentUsername}/blocked`);
                    const blockedByMeData = blockedByMeRes.ok ? await blockedByMeRes.json() : [];
                    const blockedList = Array.isArray(blockedByMeData) ? blockedByMeData : blockedByMeData.users || [];
                    setBlockedByMe(blockedList);

                    const blockedMeRes = await fetch(`http://localhost:8080/api/users/${currentUsername}/blockedBy`);
                    const blockedMeData = blockedMeRes.ok ? await blockedMeRes.json() : [];
                    setBlockedMe(Array.isArray(blockedMeData) ? blockedMeData : blockedMeData.users || []);

                    // If viewing own profile, fetch detailed blocked user objects
                    if (currentUsername === username) {
                        const blockedUsersDetailRes = await fetch(`http://localhost:8080/api/users/${currentUsername}/blockedUsers`);
                        const blockedUsersDetail = blockedUsersDetailRes.ok ? await blockedUsersDetailRes.json() : [];
                        setBlockedUsers(blockedUsersDetail);
                    }
                } catch (err) {
                    console.warn('Could not load block lists', err);
                }
            }
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
        // if the profile user is blocked by me or blocked me, prevent follow
        if (blockedByMe.includes(username) || blockedMe.includes(username)) return;
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
        // if blocked, unfollow should be disabled/ignored
        if (blockedByMe.includes(username) || blockedMe.includes(username)) return;
        const res = await fetch(
            `http://localhost:8080/api/users/${currentUsername}/follow/${username}`,
            { method: 'DELETE' }
        );
        if (res.ok) {
            setIsFollowing(false);
            setFollowers(prev => prev.filter(f => f.userName !== currentUsername));
        }
    };

    // Block a user (POST). Mirrors follow endpoint pattern.
    const handleBlock = async () => {
        if (!currentUsername) return;
        try {
            const res = await fetch(
                `http://localhost:8080/api/users/${currentUsername}/block/${username}`,
                { method: 'PUT' }
            );
            if (res.ok) {
                // add to blockedByMe and hide follow
                setBlockedByMe(prev => Array.from(new Set([...prev, username])));
                // if we were following, optimistically update
                setIsFollowing(false);
                setFollowers(prev => prev.filter(f => f.userName !== currentUsername));
            } else {
                console.warn('Block request failed', res.status);
            }
        } catch (err) {
            console.error('Block error', err);
        }
    };

    // Unblock a user (DELETE). If targetUsername is provided, unblock that user (used in blocked users list).
    const handleUnblock = async (targetUsername) => {
        if (!currentUsername) return;
        const toUnblock = targetUsername || username;
        try {
            const res = await fetch(
                `http://localhost:8080/api/users/${currentUsername}/block/${toUnblock}`,
                { method: 'DELETE' }
            );
            if (res.ok) {
                setBlockedByMe(prev => prev.filter(u => u !== toUnblock));
                // if we removed from blockedUsers list view, refresh that list
                setBlockedUsers(prev => prev.filter(u => u.userName !== toUnblock));
            } else {
                console.warn('Unblock request failed', res.status);
            }
        } catch (err) {
            console.error('Unblock error', err);
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

    // Backend now handles all filtering (blocked users, requester visibility, etc.)
    // so we don't need client-side filtering anymore.
    const isViewerProfileOwner = currentUsername === username;

    const isBlockedOrBlocking = !isViewerProfileOwner && (
        blockedByMe.includes(username) || blockedMe.includes(username)
    );

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
                        {blockedByMe.includes(username) ? (
                            // I blocked this user -> show Unblock only
                            <button onClick={handleUnblock}>Unblock</button>
                        ) : blockedMe.includes(username) ? (
                            // They blocked me -> show disabled Blocked state
                            <button disabled>Blocked</button>
                        ) : (
                            // Normal state: show Follow/Unfollow and Block
                            <>
                                {isFollowing ? (
                                    <button onClick={handleUnfollow}>Unfollow</button>
                                ) : (
                                    <button onClick={handleFollow}>Follow</button>
                                )}
                                <button onClick={handleBlock}>Block</button>
                            </>
                        )}
                    </div>
                )}
            </header>

            <div className="profile-content">
                {currentUsername === username && (
                    <CreatePostForm onPostCreated={fetchProfileData} />
                )}

                {isBlockedOrBlocking ? (
                    <div className="blocked-message card">
                        {blockedByMe.includes(username) ? (
                            <p>You have blocked this user. Their content is hidden.</p>
                        ) : (
                            <p>This user's profile is not available because they have blocked you.</p>
                        )}
                    </div>
                ) : (
                    <div className="posts-grid">
                        <h3>Posts</h3>
                {currentUsername === username && blockedUsers.length > 0 && (
                    <div className="blocked-users-section card">
                        <h4>Blocked Users ({blockedUsers.length})</h4>
                        <div className="blocked-users-list">
                            {blockedUsers.map((user) => (
                                <div key={user.userId} className="blocked-user-item">
                                    <div className="blocked-user-info">
                                        <span className="blocked-user-name">{user.fullName}</span>
                                        <span className="blocked-user-username">@{user.userName}</span>
                                    </div>
                                    <button
                                        className="unblock-btn"
                                        onClick={() => handleUnblock(user.userName)}
                                    >
                                        Unblock
                                    </button>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
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
                )}
            </div>

            {/* Followers Modal */}
            {showFollowersModal && (
                <div className="modal">
                    <div className="modal-content">
                        <h3>Followers</h3>
                        <button className="close-btn" onClick={() => setShowFollowersModal(false)}>X</button>
                        {blockedMe.includes(username) ? (
                            <div className="blocked-notice">
                                <p>Action not allowed — this user has blocked you. You cannot view their followers.</p>
                            </div>
                        ) : (
                            <ul>
                                {followers.length > 0 ? followers.map(f => (
                                    <li key={f.userName}>{f.userName}</li>
                                )) : <li>No followers available</li>}
                            </ul>
                        )}
                    </div>
                </div>
            )}

            {/* Following Modal */}
            {showFollowingModal && (
                <div className="modal">
                    <div className="modal-content">
                        <h3>Following</h3>
                        <button className="close-btn" onClick={() => setShowFollowingModal(false)}>X</button>
                        {blockedMe.includes(username) ? (
                            <div className="blocked-notice">
                                <p>Action not allowed — this user has blocked you. You cannot view their following list.</p>
                            </div>
                        ) : (
                            <ul>
                                {following.length > 0 ? following.map(f => (
                                    <li key={f.userName}>{f.userName}</li>
                                )) : <li>No following available</li>}
                            </ul>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}
