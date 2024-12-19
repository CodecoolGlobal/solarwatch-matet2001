/** @type {import('tailwindcss').Config} */
import forms from '@tailwindcss/forms';
export default {
  mode: 'jit',
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        light: {
          bg: "#f1f2f4", // Primary Background
          secondaryBg: "#008343", // Secondary Background
          text: "#000000", // Default Text
          mutedText: "#2a3032", // Muted Text
          border: "#374151", // Borders/Dividers
        },
        dark: {
          bg: "#1e2838", // Primary Background
          secondaryBg: "#1F2937", // Secondary Background
          text: "#ffffff", // Default Text
          mutedText: "#94D1C2", // Muted Text
          border: "#E5E7EB", // Borders/Dividers
        },

        link: "#FFD700", // Accent Color
        accent: "#FFD700", // Accent Color
        button: "#3B82F6", // Primary Button
        buttonHover: "#2563EB", // Button Hover
      },

      fontFamily: {
        sans: ["Montserrat", "sans-serif"],
      },
    },
  },
  darkMode: "class",
  plugins: [forms],
};

