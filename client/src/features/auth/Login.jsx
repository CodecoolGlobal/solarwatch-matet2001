import { useState } from "react";
import { Link } from "react-router-dom";

import BiggerOnHover from "../main/components/atoms/BiggerOnHover.jsx";
import { useLogin } from "./hooks/useLogin.jsx";
import { useAuth } from "./AuthContext.jsx";
import SolarWatchLogo from "../main/components/atoms/SolarWatchLogo.jsx";
import SolarWatchButton from "../main/components/atoms/SolarWatchButton.jsx";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const { login, error, success } = useLogin();
    const { saveUser } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const userData = await login(username, password);
        saveUser(userData);

        if (userData) {
            setTimeout(() => {
                window.location.href = "/";
            }, 1000);
        }
    };

    return (
        <div>
            <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                    <SolarWatchLogo size={60} />
                    <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight">
                        Sign in to your account
                    </h2>
                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form
                        className="space-y-6"
                        action="#"
                        method="POST"
                        onSubmit={handleSubmit}
                    >
                        <div>
                            <div className="flex items-start">
                                <label
                                    htmlFor="username"
                                    className="block text-sm/6 font-medium"
                                >
                                    Name
                                </label>
                            </div>
                            <div className="mt-2">
                                <input
                                    id="username"
                                    name="username"
                                    type="text"
                                    autoComplete="email"
                                    required
                                    onChange={(e) => {
                                        console.log(e.target.value);
                                        setUsername(e.target.value);
                                    }}
                                    className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                                />
                            </div>
                        </div>

                        <div>
                            <div className="flex items-start">
                                <label
                                    htmlFor="password"
                                    className="block text-sm/6 font-medium"
                                >
                                    Password
                                </label>
                            </div>
                            <div className="mt-2">
                                <input
                                    id="password"
                                    name="password"
                                    type="password"
                                    autoComplete="current-password"
                                    required
                                    onChange={(e) => setPassword(e.target.value)}
                                    className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                                />
                            </div>
                        </div>

                        <div>
                            <SolarWatchButton
                                type="submit"
                                className="flex w-full justify-center rounded-md px-3 py-1.5 text-sm/6 font-semibold text-white shadow-sm focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                            >
                                Sign in
                            </SolarWatchButton>
                        </div>
                    </form>

                    {/* Display error and success messages */}
                    {error && <p className="text-red-500">{error}</p>}
                    {success && <p className="text-green-500">{success}</p>}

                    <div className="mt-10 text-center text-lg text-black dark:text-dark-mutedText">
                        Don&#39;t have an account yet?
                        <BiggerOnHover>
                            <Link
                                className="font-semibold text-button hover:text-button-hover"
                                to="/register"
                            >
                                {" "}
                                Register
                            </Link>
                        </BiggerOnHover>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
