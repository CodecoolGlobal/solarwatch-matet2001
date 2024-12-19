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
import {useAuth} from "../../../auth/AuthContext.jsx";


function classNames(...classes) {
    return classes.filter(Boolean).join(" ");
}

export default function Navbar() {
    const { logout, user, isLoggedIn } = useAuth();

    return (
        <Disclosure as="nav" className="">
            <div className="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
                <div className="relative flex h-16 items-center justify-between">
                    <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
                        {/* Mobile menu button*/}
                        <DisclosureButton className="group relative inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
                            <span className="absolute -inset-0.5" />
                            <span className="sr-only">Open main menu</span>
                            <Bars3Icon
                                aria-hidden="true"
                                className="block size-6 group-data-[open]:hidden"
                            />
                            <XMarkIcon
                                aria-hidden="true"
                                className="hidden size-6 group-data-[open]:block"
                            />
                        </DisclosureButton>
                    </div>
                    <div className="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
                        <div className="flex shrink-0 items-center">
                            {/* SolarWatchLogo */}
                            <Link to={"/"}>
                                <BiggerOnHover>
                                    <div className="flex items-center gap-x-2">
                                        <SolarWatchLogo size={20}/>
                                        <h2>Solar Watch</h2>
                                    </div>
                                </BiggerOnHover>
                            </Link>
                        </div>
                    </div>
                    <div className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                        {isLoggedIn() ? (
                            <SolarWatchButton onClick={logout}>
                                Logout
                            </SolarWatchButton>
                        ) : (
                            <div className="flex gap-x-5 items-center">
                                <BiggerOnHover>
                                    <Link to="/login">Login</Link>
                                </BiggerOnHover>
                                <SolarWatchButton>
                                    <Link to="/register">Sign Up</Link>
                                </SolarWatchButton>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </Disclosure>
    );
}
