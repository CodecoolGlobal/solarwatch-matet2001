import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Layout from "./features/main/components/organisms/Layout.jsx";
import Home from "./features/main/components/organisms/Home.jsx";
import {AuthProvider} from "./features/auth/AuthContext.jsx";
import Login from "./features/auth/Login.jsx";
import Register from "./features/auth/Register.jsx";

function App() {
  return (
      <AuthProvider>
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
                        path='/'
                        element={<Home />}
                    />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                </Route>
            </Routes>
          </BrowserRouter>
      </AuthProvider>
  );
}

export default App;
