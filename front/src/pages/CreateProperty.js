import React, { useState } from 'react';
import axios from 'axios';

const AddNekretninaForm = () => {
  const [formData, setFormData] = useState({
    lokacija: '',
    povrsina: '',
    cena: '',
    prodajaIzdaja: 'PRODAJA',
    tip: 'KUCA',
    slikeUBase64: []
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const fileToBase64 = async (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        const base64String = reader.result.split(',')[1];
        resolve(base64String);
      };
      reader.onerror = (error) => reject(error);
    });
  };

  const handleImageChange = async (e) => {
    const files = Array.from(e.target.files);
    const base64Images = [];
  
    for (const file of files) {
      try {
        const base64String = await fileToBase64(file);
        base64Images.push(base64String);
      } catch (error) {
        console.error('Error converting image to base64:', error);
      }
    }
  
    setFormData({ ...formData, slikeUBase64: base64Images });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Form Data:', formData);
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        console.error('Token not found.');
        return;
      }
      const response = await axios.post('http://localhost:8080/api/nekretnine/novanekretnina', formData, {
        headers: {
          'authorization': token 
        }
      });
      console.log('Nekretnina successfully created!');
      setFormData({
        lokacija: '',
        povrsina: '',
        cena: '',
        prodajaIzdaja: 'PRODAJA',
        tip: 'KUCA',
        slikeUBase64: []
      });
    } catch (error) {
      console.error('Error creating nekretnina:', error);
    }
  };

  return (
    <div className="container mt-5">
      <h2>Add Nekretnina Form</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="lokacija">Lokacija:</label>
          <input
            type="text"
            className="form-control"
            id="lokacija"
            name="lokacija"
            value={formData.lokacija}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="povrsina">Površina:</label>
          <input
            type="number"
            step="0.01"
            className="form-control"
            id="povrsina"
            name="povrsina"
            value={formData.povrsina}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="cena">Cena:</label>
          <input
            type="number"
            step="0.01"
            className="form-control"
            id="cena"
            name="cena"
            value={formData.cena}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="tip">Tip:</label>
          <select
            className="form-control"
            id="tip"
            name="tip"
            value={formData.tip}
            onChange={handleChange}
          >
            <option value="KUCA">Kuća</option>
            <option value="STAN">Stan</option>
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="prodajaIzdaja">Prodaja/Izdaja:</label>
          <select
            className="form-control"
            id="prodajaIzdaja"
            name="prodajaIzdaja"
            value={formData.prodajaIzdaja}
            onChange={handleChange}
          >
            <option value="PRODAJA">Prodaja</option>
            <option value="IZDAJA">Izdaja</option>
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="slikeUBase64">Izaberite slike:</label>
          <input
            type="file"
            className="form-control-file"
            id="slikeUBase64"
            name="slikeUBase64"
            accept="image/*"
            multiple
            onChange={handleImageChange}
          />
        </div>
        <button type="submit" className="btn btn-primary">Kreiraj Nekretninu</button>
      </form>
    </div>
  );
};

export default AddNekretninaForm;
