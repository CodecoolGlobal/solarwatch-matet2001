import { useState } from "react";
import axios from "axios";

export const useLogin = () => {
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const login = async (username, password) => {
        setError("");
        setSuccess("");
        try {
            const response = await axios.post(`/api/auth/login`, {
                username,
                password,
            });

            const { jwtToken, userName, roles, message } = response.data;

            setSuccess(message);

            return {
                jwtToken,
                userName,
                roles,
            };
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Authentication failed. Please try again.",
            );
        }
    };

    return { login, error, success };
};
