import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

export default function Navbar() {
    const { currentUser, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">InstaClone</Link>
                {currentUser && (
                    <div className="navbar-menu">
                        <Link to={`/${currentUser}`} className="navbar-item">My Profile</Link>
                        <button onClick={handleLogout} className="navbar-item logout-btn">Logout</button>
                    </div>
                )}
            </div>
        </nav>
    );
}