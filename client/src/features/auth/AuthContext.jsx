import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null);

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedUser = retrieveStoredUser();
        if (storedUser) setUser(storedUser);
    }, []);

    const retrieveStoredUser = () => {
        const username = localStorage.getItem("username");
        const jwtToken = localStorage.getItem("jwtToken");
        const roles = JSON.parse(localStorage.getItem("roles") || "[]");

        if (username && jwtToken) {
            return { username, jwtToken, roles };
        }
        return null;
    };

    const saveUser = (userData) => {
        if (!userData) return;

        const { username, jwtToken, roles } = userData;

        setUser({ username, jwtToken, roles });

        if (username) localStorage.setItem("username", username);
        if (jwtToken) localStorage.setItem("jwtToken", jwtToken);
        if (roles) localStorage.setItem("roles", JSON.stringify(roles));
    };

    const isLoggedIn = () => !!localStorage.getItem("username");

    const logout = () => {
        setUser(null);
        localStorage.removeItem("username");
        localStorage.removeItem("jwtToken");
        localStorage.removeItem("roles");
    };

    return (
        <AuthContext.Provider
            value={{ user, saveUser, logout, isLoggedIn, retrieveStoredUser }}
        >
            {children}
        </AuthContext.Provider>
    );
};