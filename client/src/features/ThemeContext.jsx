import React, { createContext, useState, useEffect, useContext } from "react";

const ThemeContext = createContext("light");

export const useTheme = () => {
    return useContext(ThemeContext);
};

export const ThemeProvider = ({ children }) => {
    const [darkMode, setDarkMode] = useState(false);

    // Initialize dark mode state from localStorage when the component mounts
    useEffect(() => {
        const savedTheme = localStorage.getItem("darkMode") === "true";
        setDarkMode(savedTheme);

        // Apply the correct class to the <html> element
        if (savedTheme) {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }
    }, []);

    const toggleDarkMode = () => {
        if (!darkMode) {
            document.documentElement.classList.add("dark");
            localStorage.setItem("darkMode", "true");
        } else {
            document.documentElement.classList.remove("dark");
            localStorage.setItem("darkMode", "false");
        }
        setDarkMode(!darkMode);
    };

    return (
        <ThemeContext.Provider value={{ darkMode, toggleDarkMode }}>
            {children}
        </ThemeContext.Provider>
    );
};
