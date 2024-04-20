import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import LoginForm from './components/Login';
import RegisterUser from './pages/RegisterUser';
import RegisterAgent from './pages/RegisterAgent';
import RegisterAdmin from './pages/RegisterAdmin';
import RegisterVlasnik from './pages/RegisterVlasnik';
import CreateProperty from './pages/CreateProperty';
import SingleProperty from './pages/SingleProperty';
import SearchFilter from './components/SearchFilter';

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginForm />} />
        <Route path="/register/korisnik" element={<RegisterUser />} />
        <Route path="/register/agent" element={<RegisterAgent />} />
        <Route path="/register/admin" element={<RegisterAdmin />} />
        <Route path="/register/vlasnik" element={<RegisterVlasnik />} />
        <Route path="/nekretnina/create" element={<CreateProperty />} />
        <Route path="/nekretnina/:id" element={<SingleProperty />} />
        <Route path="/pretraga" element={<SearchFilter />} />
      </Routes>
    </Router>
  );
}

export default App;
