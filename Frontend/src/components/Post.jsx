import './Post.css';

export default function Post({ post, currentUsername, onPostUpdated }) {

    // Function to delete a post
    const handleDelete = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/posts/${post.postId}`, {
                method: 'DELETE',
            });
            if (response.ok) {
                onPostUpdated(); // Refresh post list in parent
            } else {
                console.error("Failed to delete post");
            }
        } catch (error) {
            console.error("Error deleting post:", error);
        }
    };

    console.log('Post:', post);
    console.log('Logged-in user:', currentUsername);

    return (
        <div className="post-card">
            <h3 className="post-title">{post.postTitle}</h3>
            <p className="post-content">{post.postContent}</p>

            <p className="post-timestamp">
                {new Date(post.createdAt).toLocaleString()}
            </p>

            {/* Only show Delete button if this post belongs to the logged-in user */}
            {post.username === currentUsername && (  // <-- change to match backend field
                <button className="delete-btn" onClick={handleDelete}>
                    Delete
                </button>
            )}
        </div>
    );
}
