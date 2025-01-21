import React from "react";

const SolarInput = ({ label, id, className = "", ...props }) => {
    return (
        <div>
            {label && (
                <label
                    htmlFor={id}
                    className="block text-md font-medium"
                >
                    {label}
                </label>
            )}
            <div className="mt-2">
                <input
                    id={id}
                    className={`block w-full rounded-md border-0 py-2.5 bg-gray-50 text-gray-900 dark:text-white dark:bg-gray-700  shadow-sm ring-1 ring-inset ring-gray-300 dark:ring-gray-500 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm ${className}`}
                    {...props}
                />
            </div>
        </div>
    );
};

export default SolarInput;
