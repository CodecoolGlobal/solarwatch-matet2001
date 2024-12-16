import {Outlet} from "react-router";
import Header from "./components/Header.jsx";

function Layout() {
    return (
        <div className="h-full bg-white">
            <Header />
            <Outlet />
        </div>
    )
}

export default Layout;