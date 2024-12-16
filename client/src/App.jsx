import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Layout from "./features/main/Layout.jsx";
import Home from "./features/main/Home.jsx";
import Sign from "./features/auth/Sign.jsx";

function App() {
  return (
      <BrowserRouter
          future={{
            v7_startTransition: true,
            v7_relativeSplatPath: true,
          }}
      >
        <Routes>
            <Route
                element={<Layout />}
            >
                <Route
                    path='/home'
                    element={<Home />}
                />
                <Route
                    path='/signup'
                    element={<Sign />}
                />
            </Route>
        </Routes>
      </BrowserRouter>
  );
}

export default App;
