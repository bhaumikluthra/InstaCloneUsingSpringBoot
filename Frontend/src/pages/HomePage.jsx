import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function HomePage() {
    const { currentUser } = useAuth();
    const currentUsername = currentUser?.username;

    const [users, setUsers] = useState([]);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/users');
                const data = await response.json();

                // If there's no logged-in user, just show all users
                if (!currentUsername) {
                    setUsers(data);
                    return;
                }

                // Fetch block lists for current user
                let blockedByMe = [];
                let blockedMe = [];
                try {
                    const bOwn = await fetch(`http://localhost:8080/api/users/${currentUsername}/blocked`);
                    if (bOwn.ok) blockedByMe = await bOwn.json();
                } catch (e) { console.warn('Could not fetch blocked list', e); }
                try {
                    const bMe = await fetch(`http://localhost:8080/api/users/${currentUsername}/blockedBy`);
                    if (bMe.ok) blockedMe = await bMe.json();
                } catch (e) { console.warn('Could not fetch blockedBy list', e); }

                const visible = (Array.isArray(data) ? data : []).filter(u =>
                    u.userName !== currentUsername &&
                    !blockedByMe.includes(u.userName) &&
                    !blockedMe.includes(u.userName)
                );

                setUsers(visible);
            } catch (error) {
                console.error('Failed to load users.', error);
            }
        };
        fetchUsers();
    }, [currentUsername]);

    return (
        <div className="container">
            <main>
                <div>
                    <h1>Feed</h1>
                </div>
                <div className="user-list-container card">
                    <h2>Discover Users</h2>
                    <ul className="user-list">
                        {users.map(user => (
                            <li key={user.userName} className="user-item">
                                <Link to={`/${user.userName}`} className="username">{user.userName}</Link>
                                <span className="name">({user.name})</span>
                            </li>
                        ))}
                    </ul>
                </div>
            </main>
        </div>
    );
}