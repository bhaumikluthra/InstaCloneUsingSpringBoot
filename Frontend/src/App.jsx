import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import ProfilePage from './pages/ProfilePage';
import './App.css';

// We will modify this helper component
function PrivateRoute({ children }) {
    const { currentUser } = useAuth();

    // If the user is not logged in, redirect them to the login page
    if (!currentUser) {
        return <Navigate to="/login" />;
    }

    // If the user IS logged in, render the Navbar and the page content
    return (
        <>
            <Navbar />
            {children}
        </>
    );
}

export default function App() {
    return (
        // The Navbar is no longer here. It's now handled by PrivateRoute.
        <Routes>
            {/* The LoginPage route has no Navbar */}
            <Route path="/login" element={<LoginPage />} />

            {/* These routes are protected and will now include the Navbar */}
            <Route path="/" element={<PrivateRoute><HomePage /></PrivateRoute>} />
            <Route path="/:username" element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
        </Routes>
    );
}