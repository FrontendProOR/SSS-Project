import React, { useState } from 'react';
import axios from 'axios';

const Register = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    firstName: '',
    lastName: '',
    brojTelefona: '',
    adresa: '',
    uloga: 'AGENT' 

  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      console.log(formData.firstName);
      const response = await axios.post('http://localhost:8080/api/korisnici/registracijaagenta', formData, {
        headers: {
          'authorization': token
        }
      });
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="container mt-5">
      <h2>Registruj Agenta</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="firstName" className="form-label">Ime</label>
          <input type="text" className="form-control" id="firstName" name="firstName" value={formData.firstName} onChange={handleChange} required />
        </div>
        <div className="mb-3">
          <label htmlFor="lastName" className="form-label">Prezime</label>
          <input type="text" className="form-control" id="lastName" name="lastName" value={formData.lastName} onChange={handleChange} required />
        </div>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">Email</label>
          <input type="email" className="form-control" id="email" name="email" value={formData.email} onChange={handleChange} required />
        </div>
        <div className="mb-3">
          <label htmlFor="password" className="form-label">Lozinka</label>
          <input type="password" className="form-control" id="password" name="password" value={formData.password} onChange={handleChange} required />
        </div>
        <div className="mb-3">
          <label htmlFor="brojTelefona" className="form-label">Broj telefona</label>
          <input type="tel" className="form-control" id="brojTelefona" name="brojTelefona" value={formData.brojTelefona} onChange={handleChange} required />
        </div>
        <div className="mb-3">
          <label htmlFor="adresa" className="form-label">Adresa</label>
          <input type="text" className="form-control" id="adresa" name="adresa" value={formData.adresa} onChange={handleChange} required />
        </div>
        <button type="submit" className="btn btn-primary">Registruj Agenta</button>
      </form>
    </div>
  );
};

export default Register;