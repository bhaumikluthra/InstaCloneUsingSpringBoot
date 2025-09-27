import { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // <-- 1. IMPORT useNavigate
import { useAuth } from '../context/AuthContext';
import '../components/LoginPage.css';

export default function LoginPage() {
    const [isRegistering, setIsRegistering] = useState(false);
    const [username, setUsername] = useState('');
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate(); // <-- 2. GET the navigate function

    // ... (Your handleRegister function stays the same)
    const handleRegister = async (e) => {
        e.preventDefault();
        const userDto = { userName: username, name, password };
        setMessage('Registering...');
        try {
            const response = await fetch('http://localhost:8080/api/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userDto),
            });
            if (response.ok) {
                setMessage('Registration successful! Please log in.');
                setIsRegistering(false);
            } else {
                const errorText = await response.text();
                setMessage(`Registration failed: ${errorText}`);
            }
        } catch (error) {
            setMessage('A network error occurred.');
        }
    };


    const handleLogin = async (e) => {
        e.preventDefault();
        const loginDto = { userName: username, password: password };
        setMessage('Logging in...');
        try {
            const response = await fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(loginDto),
            });

            if (response.ok) {
                const userData = await response.json();
                login(userData.username);
                navigate('/'); // <-- 3. NAVIGATE to the home page on success
            } else {
                const errorText = await response.text();
                setMessage(`Login failed: ${errorText}`);
            }
        } catch (error) {
            setMessage('A network error occurred.');
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h1>InstaClone</h1>
                {isRegistering ? (
                    <>
                        <h2>Sign up</h2>
                        <form onSubmit={handleRegister}>
                            <input type="text" value={username} onChange={e => setUsername(e.target.value)} placeholder="Username" required />
                            <input type="text" value={name} onChange={e => setName(e.target.value)} placeholder="Full Name" required />
                            <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" required />
                            <button type="submit">Register</button>
                        </form>
                        <p>Already have an account? <a href="#" onClick={() => setIsRegistering(false)}>Log in</a></p>
                    </>
                ) : (
                    <>
                        <h2>Login</h2>
                        <form onSubmit={handleLogin}>
                            <input type="text" value={username} onChange={e => setUsername(e.target.value)} placeholder="Username" required />
                            <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" required />
                            <button type="submit">Log In</button>
                        </form>
                        <p>Don't have an account? <a href="#" onClick={() => setIsRegistering(true)}>Sign up</a></p>
                    </>
                )}
                {message && <p className="message">{message}</p>}
            </div>
        </div>
    );
}