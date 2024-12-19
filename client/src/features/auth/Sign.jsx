import { useState } from 'react';
import axiosInstance from "../../../AxiosInstance.jsx";

function Sign() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [mode, setMode] = useState("register");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const body = {username, password}

        try {
            const response = await axiosInstance.post(`/auth/${mode}`, body);

            if (!response.ok) {
                setError('Something went wrong');
            }

            const data = await response.json();
            setSuccess(data.message)
            localStorage.setItem('jwtToken', data.token);
            setTimeout(() => {
                window.location.href = '/home';
            }, 1000);

            console.log('Login successful', data);
        } catch (err) {
            setError(`Authentication failed. ${err}`);

            console.error(err);
        }
    }

    return (
        <div>
            <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                    <img className="mx-auto h-20 w-auto"
                         src="/logo.svg"
                         alt="Your Company"/>
                    {mode === "register" ?
                        <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">Register an
                            account</h2> :
                        <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">Sign in to
                            your
                            account</h2>
                    }

                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form className="space-y-6" action="#" method="POST" onSubmit={handleSubmit}>
                        <div>
                            <div className="flex items-start">
                                <label htmlFor="username" className="block text-sm/6 font-medium text-gray-900">
                                    Name</label>
                            </div>
                            <div className="mt-2">
                                <input id="username" name="username" type="text" autoComplete="email" required onChange={(e) => setUsername(e.target.value)}
                                       className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"/>
                            </div>
                        </div>

                        <div>
                            <div className="flex items-start">
                                <label htmlFor="password"
                                       className="block text-sm/6 font-medium text-gray-900">Password</label>
                            </div>
                            <div className="mt-2">
                                <input id="password" name="password" type="password" autoComplete="current-password"
                                       required onChange={(e) => setPassword(e.target.value)}
                                       className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"/>
                            </div>
                        </div>

                        <div>
                            <button type="submit"
                                    className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Sign
                                in
                            </button>
                        </div>
                    </form>

                    {error && <p className="text-red-500">{error}</p>}
                    {success && <p className="text-green-500">{success}</p>}


                    {mode === "register" ?
                        <p className="mt-10 text-center text-sm/6 text-gray-500">
                            Already have an account?
                            <button onClick={() => setMode("login")}
                                    className="font-semibold text-indigo-600 hover:text-indigo-500"> Login</button>
                        </p>
                        :
                        <p className="mt-10 text-center text-sm/6 text-gray-500">
                            Don&#39;t have an account yet?
                            <button onClick={() => setMode("register")}
                                    className="font-semibold text-indigo-600 hover:text-indigo-500">  Register</button>
                        </p>
                    }
                </div>
            </div>
        </div>
    )
}

export default Sign;