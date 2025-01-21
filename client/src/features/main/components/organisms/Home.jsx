import {useEffect, useState} from "react";
import SolarCard from "../molecules/SolarCard.jsx";
import axiosInstance from "../../../../../AxiosInstance.jsx";
import SolarForm from "../molecules/SolarForm.jsx";
import {useAuth} from "../../../auth/AuthContext.jsx";
import {useNavigate} from "react-router";

function Home() {
    const [solarData, setSolarData] = useState(null);
    const { isLoggedIn } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isLoggedIn()) {
            navigate("/login");
        }
    }, [isLoggedIn, navigate]);

    async function handleSubmit(e) {
        e.preventDefault();

        try {
            const formData = new FormData(e.target);
            const data = Object.fromEntries(formData.entries());

            const response = await axiosInstance.get('/solar-watch', {
                params: {
                    cityName: data.cityName,
                    date: data.date || undefined
                }
            });

            setSolarData(response.data);
        } catch (err) {
            console.error(err);
        }
    }

    return (
        <div className="flex min-h-full flex-col justify-center items-center px-6 py-12 lg:px-8 text-black">
            <SolarForm handleSubmit={handleSubmit}/>
            <SolarCard solarData={solarData}/>
        </div>
    )
}

export default Home;