import { useState } from 'react';
import { useAuth } from '../context/AuthContext';

export default function CreatePostForm({ onPostCreated }) {
    const { currentUser } = useAuth();
    const currentUsername = currentUser?.username; // <-- extract username

    const [postTitle, setPostTitle] = useState('');
    const [postContent, setPostContent] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        const postDto = { postTitle, postContent };

        try {
            const response = await fetch(`http://localhost:8080/api/users/${currentUsername}/posts`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(postDto)
            });

            if (response.ok) {
                setMessage('Post created successfully!');
                setPostTitle('');
                setPostContent('');
                onPostCreated(); // Tell the parent component to refresh the post list
                setTimeout(() => setMessage(''), 3000); // Clear message after 3 seconds
            } else {
                setMessage('Failed to create post.');
            }
        } catch (error) {
            setMessage('A network error occurred.');
            console.error("Failed to create post", error);
        }
    };

    return (
        <div className="card create-post-form">
            <h2>Create New Post</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Post Title"
                    value={postTitle}
                    onChange={e => setPostTitle(e.target.value)}
                    required
                />
                <textarea
                    placeholder="What's on your mind?"
                    value={postContent}
                    onChange={e => setPostContent(e.target.value)}
                    rows="4"
                    required
                ></textarea>
                <button type="submit">Post</button>
                {message && <p className="message">{message}</p>}
            </form>
        </div>
    );
}