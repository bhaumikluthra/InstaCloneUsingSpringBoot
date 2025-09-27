import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

export default function HomePage() {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/users');
                const data = await response.json();
                setUsers(data);
            } catch (error) {
                console.error('Failed to load users.');
            }
        };
        fetchUsers();
    }, []);

    return (
        <div className="container">
            <main>
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