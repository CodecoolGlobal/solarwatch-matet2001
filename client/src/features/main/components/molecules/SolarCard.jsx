
function SolarCard({solarData}) {
    return (
        <>
            {solarData && <div className="bg-blue-400 max-w-2xl p-10 rounded-md mt-10 shadow-md">
                <h1 className="text-3xl font-bold">Results for {solarData.city.name}</h1>
                <div className="flex justify-between mt-3">
                    <p>Longitude: {solarData.city.lon.toFixed(2)}</p>
                    <p>Latitude: {solarData.city.lat.toFixed(2)}</p>
                </div>
                <div className="mt-5 flex flex-col items-start space-y-1 text-xl">
                    <p>Sunrise: {solarData.sunTimes.sunrise}</p>
                    <p>Sunset: {solarData.sunTimes.sunset}</p>
                </div>
            </div>}
        </>
    )
}

export default SolarCard;