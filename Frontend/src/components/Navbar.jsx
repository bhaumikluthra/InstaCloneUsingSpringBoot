
import { useState } from 'react'; // 1. Import useState
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

export default function Navbar() {
    const { currentUser, logout } = useAuth();
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState(''); // 2. Create state to store search text

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    // 3. Create a function to handle the search navigation
    const handleSearch = () => {
        // Navigate only if there is text in the search box
        if (searchQuery.trim()) {
            navigate(`/${searchQuery}`);
            setSearchQuery(''); // Clear the input after searching
        }
    };

    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">TweetInsta</Link>

                <div className="navbar-search">
                    {/* 4. Connect input to state */}
                    <input
                        type="text"
                        className="navbar-search-input"
                        placeholder="Search user"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                    {/* 5. Call the handler function on click */}
                    <button className="search-btn" onClick={handleSearch}>
                        Enter
                    </button>
                </div>

                {currentUser && (
                    <div className="navbar-menu">
                        <Link to={`/${currentUser.username}`} className="navbar-item">
                            My Profile
                        </Link>
                        <button onClick={handleLogout} className="navbar-item logout-btn">Logout</button>
                    </div>
                )}

            </div>
        </nav>
    );
}