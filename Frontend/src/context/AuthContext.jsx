import { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [currentUser, setCurrentUser] = useState(null);

    // Load currentUser from localStorage on app start
    useEffect(() => {
        const storedUser = localStorage.getItem('currentUser');
        if (storedUser) setCurrentUser(JSON.parse(storedUser));
    }, []);

    const login = (username) => {
        setCurrentUser({ username });
        localStorage.setItem('currentUser', JSON.stringify({ username }));
    };

    const logout = () => {
        setCurrentUser(null);
        localStorage.removeItem('currentUser');
    };

    return (
        <AuthContext.Provider value={{ currentUser, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}
