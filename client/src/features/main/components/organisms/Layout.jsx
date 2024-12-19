import {Outlet} from "react-router";
import Navbar from "../molecules/Navbar.jsx";

function Layout() {
    return (
        <div className="h-full bg-white">
            <Navbar />
            <Outlet />
        </div>
    )
}

export default Layout;