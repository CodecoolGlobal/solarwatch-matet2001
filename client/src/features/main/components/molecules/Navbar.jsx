import {
    Disclosure,
    DisclosureButton,
    DisclosurePanel,
} from "@headlessui/react";
import { Bars3Icon, XMarkIcon } from "@heroicons/react/24/outline";
import { Link } from "react-router-dom";
import SolarWatchLogo from "../atoms/SolarWatchLogo.jsx";
import SolarWatchButton from "../atoms/SolarWatchButton.jsx";
import BiggerOnHover from "../atoms/BiggerOnHover.jsx";
import { useAuth } from "../../../auth/AuthContext.jsx";
import { useTheme } from "../../../ThemeContext.jsx";
import Switch from "../atoms/Switch.jsx";

export default function Navbar() {
    const { logout, user, isLoggedIn } = useAuth();
    const { darkMode, toggleDarkMode } = useTheme();

    return (
        <Disclosure as="nav">
            {({ open }) => (
                <>
                    <div className="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
                        <div className="relative flex h-16 items-center justify-between">
                            {/* Mobile menu button */}
                            <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
                                <DisclosureButton className="inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
                                    <span className="sr-only">Open main menu</span>
                                    {open ? (
                                        <XMarkIcon className="block h-6 w-6" aria-hidden="true" />
                                    ) : (
                                        <Bars3Icon className="block h-6 w-6" aria-hidden="true" />
                                    )}
                                </DisclosureButton>
                            </div>

                            {/* Logo */}
                            <div className="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
                                <div className="flex shrink-0 items-center">
                                    {/* SolarWatchLogo */}
                                    <Link to={"/"}>
                                        <BiggerOnHover>
                                            <div className="flex items-center gap-x-2">
                                                <SolarWatchLogo size={20}/>
                                                <h2 className="text-xl font-bold">Solar Watch</h2>
                                            </div>
                                        </BiggerOnHover>
                                    </Link>
                                </div>
                            </div>

                            {/* Right-side desktop menu */}
                            <div className="hidden sm:flex sm:items-center sm:gap-x-12">
                                {isLoggedIn() ? (
                                    <div className="flex gap-x-5 items-center">
                                        <h2 className="text-white">Hello {user?.username}</h2>
                                        <SolarWatchButton onClick={logout}>
                                            Logout
                                        </SolarWatchButton>
                                    </div>
                                ) : (
                                    <div className="flex gap-x-5 items-center">
                                        <BiggerOnHover>
                                            <Link to="/login">
                                                Login
                                            </Link>
                                        </BiggerOnHover>
                                        <SolarWatchButton>
                                            <Link to="/register">Sign Up</Link>
                                        </SolarWatchButton>
                                    </div>
                                )}
                                <Switch isChecked={darkMode} toggle={toggleDarkMode} />
                            </div>
                        </div>
                    </div>

                    {/* Mobile menu */}
                    <DisclosurePanel className="sm:hidden">
                        <div className="space-y-1 px-2 pt-2 pb-3">
                            {isLoggedIn() ? (
                                <>
                                    <p className="block px-3 py-2 rounded-md text-base font-medium text-white">
                                        Hello {user?.username}
                                    </p>
                                    <button
                                        onClick={logout}
                                        className="block w-full text-left px-3 py-2 rounded-md text-base font-medium text-white hover:bg-gray-700"
                                    >
                                        Logout
                                    </button>
                                </>
                            ) : (
                                <>
                                    <Link
                                        to="/login"
                                        className="block px-3 py-2 rounded-md text-base font-medium text-white hover:bg-gray-700"
                                    >
                                        Login
                                    </Link>
                                    <Link
                                        to="/register"
                                        className="block px-3 py-2 rounded-md text-base font-medium text-white hover:bg-gray-700"
                                    >
                                        Sign Up
                                    </Link>
                                </>
                            )}
                            <div className="mt-2 px-3">
                                <Switch isChecked={darkMode} toggle={toggleDarkMode} />
                            </div>
                        </div>
                    </DisclosurePanel>
                </>
            )}
        </Disclosure>
    );
}
