import { useState } from "react";
import axios from "axios";

export const useRegister = () => {
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    const register = async (email, username, password) => {
        setError("");
        setSuccess("");

        try {
            const response = await axios.post("/api/auth/register", {
                email,
                username,
                password,
            });

            setSuccess(response.data.message);
            return response.data;
        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Authentication failed. Please try again.",
            );
        }
    };

    return { register, success, error };
};
