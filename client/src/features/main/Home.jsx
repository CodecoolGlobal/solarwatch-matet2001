import {useState} from "react";
import SolarCard from "./components/SolarCard.jsx";
import axiosInstance from "../../../AxiosInstance.jsx";

function Home() {
    const [error, setError] = useState('');
    const [solarData, setSolarData] = useState(null);

    async function handleSubmit(e) {
        e.preventDefault();

        try {
            const formData = new FormData(e.target);
            const data = Object.fromEntries(formData.entries());

            // Send a GET request with query parameters
            const response = await axiosInstance.get('/solar-watch', {
                params: {
                    cityName: data.cityName,
                    date: data.date || undefined // Exclude 'date' if it's empty
                }
            });

            // Response is already parsed as JSON with Axios
            setSolarData(response.data);

            console.log(response.data);
        } catch (err) {
            setError(`Failed to fetch solar data. ${err.response?.data?.message || err.message}`);
            console.error(err);
        }
    }

    return (
        <div className="flex min-h-full flex-col justify-center items-center px-6 py-12 lg:px-8">
            <div className="bg-amber-300 max-w-3xl p-10 rounded-md shadow-md">
                <h1 className="text-3xl font-bold mt-1 mb-5">Enter City and Date</h1>
                <form action="#" className="space-y-7" onSubmit={handleSubmit}>
                    <div className="flex flex-col">
                        <label htmlFor="cityName" className="mb-1 text-xl text-left">City:</label>
                        <input type="text" id="cityName" name="cityName" placeholder="Enter city name" required
                               className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"/>
                    </div>

                    <div className="flex flex-col">
                        <label htmlFor="date" className="mb-1 text-xl text-left">Date:</label>
                        <input type="date" id="date" name="date" placeholder="Enter date"
                               className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"/>
                    </div>

                    <button type="submit"
                            className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                        Get Sun Times
                    </button>
                </form>

                {error && <p className="text-red-500">{error}</p>}
            </div>

            <SolarCard solarData={solarData}/>
        </div>
    )
}

export default Home;