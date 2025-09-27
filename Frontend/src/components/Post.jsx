import './Post.css';

export default function Post({ post }) {
    return (
        <div className="post-card">
            <h3 className="post-title">{post.postTitle}</h3>
            <p className="post-content">{post.postContent}</p>
        </div>
    );
}