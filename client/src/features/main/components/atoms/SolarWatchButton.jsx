import BiggerOnHover from "./BiggerOnHover.jsx";

const SolarWatchButton = ({
                         children,
                         onClick,
                         className = "",
                         bgColor = "bg-button",
                         textColor = "text-white",
                         ...props
                     }) => (
    <BiggerOnHover>
        <button
            onClick={onClick}
            className={`inline-block font-semibold py-2 px-4 rounded-lg shadow-md transition-transform ${bgColor} hover:bg-buttonHover ${textColor} ${className}`}
            {...props}
        >
            {children}
        </button>
    </BiggerOnHover>
);

export default SolarWatchButton;
