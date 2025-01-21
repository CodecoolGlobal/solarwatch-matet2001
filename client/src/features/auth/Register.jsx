import { useState } from "react";
import { Link } from "react-router-dom";
import BiggerOnHover from "../main/components/atoms/BiggerOnHover.jsx";
import { useRegister } from "./hooks/useRegister.jsx";
import SolarWatchLogo from "../main/components/atoms/SolarWatchLogo.jsx";
import SolarWatchButton from "../main/components/atoms/SolarWatchButton.jsx";
import SolarInput from "../main/components/atoms/SolarInput.jsx";

function Register() {
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const { register, success, error } = useRegister();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const response = await register(email, username, password);

        if (response) {
            setTimeout(() => {
                window.location.href = "/login";
            }, 1000);
        }
    };

    return (
        <div>
            <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                    <SolarWatchLogo size={60} />
                    <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight">
                        Register an account
                    </h2>
                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form
                        className="space-y-6"
                        action="#"
                        method="POST"
                        onSubmit={handleSubmit}
                    >
                        <SolarInput
                            label="Email"
                            id="email"
                            name="email"
                            type="text"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            autoComplete="email"
                            required
                        />

                        <SolarInput
                            label="Username"
                            id="username"
                            name="username"
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />

                        <SolarInput
                            label="Password"
                            id="password"
                            name="password"
                            type="password"
                            autoComplete="current-password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />

                        <div>
                            <SolarWatchButton
                                type="submit"
                                className="flex w-full justify-center rounded-md px-3 py-1.5 text-sm/6 font-semibold text-white shadow-sm focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                            >
                                Sign up
                            </SolarWatchButton>
                        </div>
                    </form>

                    {error && <p className="text-red-500">{error}</p>}
                    {success && <p className="text-green-500">{success}</p>}

                    <div className="mt-10 text-center text-lg">
                        Already have an account?
                        <BiggerOnHover>
                            <Link
                                className="font-semibold text-button hover:text-button-hover"
                                to="/login"
                            >
                                {" "}
                                Login
                            </Link>
                        </BiggerOnHover>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Register;
