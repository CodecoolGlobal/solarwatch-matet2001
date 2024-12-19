import {Outlet} from "react-router";
import Navbar from "../molecules/Navbar.jsx";

function Layout() {
    return (
        <div>
            <Navbar />
            <div className="w-8/12 mx-auto">
                <Outlet />
            </div>
        </div>
    )
}

export default Layout;