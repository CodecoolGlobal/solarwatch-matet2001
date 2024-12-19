import { useState } from "react";
import axios from "axios";

export const useLogin = () => {
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const login = async (username, password) => {
        setError("");
        setSuccess("");
        try {
            const response = await axios.post("/api/auth/login", {
                username: username,
                password: password,
            });
            setSuccess(response?.data?.message);

            return response.data;
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Authentication failed. Please try again.",
            );
        }
    };

    return { login, error, success };
};
