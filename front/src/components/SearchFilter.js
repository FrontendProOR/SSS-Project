import React, { useState, useEffect } from 'react';
import axios from 'axios';

const FilterComponent = () => {
  const [lokacija, setLokacija] = useState('');
  const [povrsina, setPovrsina] = useState('');
  const [cena, setCena] = useState('');
  const [prodaja, setProdaja] = useState('');
  const [tip, setTip] = useState('');
  const [filteredProperties, setFilteredProperties] = useState([]);

  useEffect(() => {
    handleFilter(); // Fetch properties on component mount
  }, []); // Empty dependency array ensures this effect runs only once

  const handleFilter = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/nekretnine/pretraga', {
        params: {
          lokacija: lokacija,
          povrsina: povrsina,
          cena: cena,
          prodaja: prodaja,
          tip: tip,
        },
        headers: {
          authorization: 'token',
        },
      });
      setFilteredProperties(response.data);
      console.log("Uspesno ste lajkovali", response)
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const handleLike = async (propertyId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.post('http://localhost:8080/api/nekretnine/like', { nekretninaId: propertyId, like: true }, {
        headers: {
          authorization: token, // Replace 'your_token_here' with your actual token
        },
      });
      if (response.status === 200) {
        handleFilter();
      }
    } catch (error) {
      console.error('Error liking property:', error);
    }
  };

  const redirectToProperty = (id) => {
    window.location.href = `http://localhost:3000/nekretnina/${id}`;
  };

  return (
    <div className="container mt-4">
      <div className="row">
        <div className="col-md-2">
          <input
            type="text"
            className="form-control"
            placeholder="Lokacija"
            value={lokacija}
            onChange={(e) => setLokacija(e.target.value)}
          />
        </div>
        <div className="col-md-2">
          <input
            type="text"
            className="form-control"
            placeholder="Površina"
            value={povrsina}
            onChange={(e) => setPovrsina(e.target.value)}
          />
        </div>
        <div className="col-md-2">
          <input
            type="text"
            className="form-control"
            placeholder="Cena"
            value={cena}
            onChange={(e) => setCena(e.target.value)}
          />
        </div>
        <div className="col-md-2">
          <input
            type="text"
            className="form-control"
            placeholder="Prodaja"
            value={prodaja}
            onChange={(e) => setProdaja(e.target.value)}
          />
        </div>
        <div className="col-md-2">
          <input
            type="text"
            className="form-control"
            placeholder="Tip"
            value={tip}
            onChange={(e) => setTip(e.target.value)}
          />
        </div>
        <div className="col-md-2">
          <button className="btn btn-primary" onClick={handleFilter}>Apply Filter</button>
        </div>
      </div>
      <div className="row mt-4">
        {filteredProperties.map((property) => (
          <div key={property.id} className="col-md-4 mb-4">
            <div className="card">
              <div className="card-body">
                <h5 className="card-title">ID: {property.id}</h5>
                <p className="card-text">Lokacija: {property.lokacija}</p>
                <p className="card-text">Površina: {property.povrsina}</p>
                <p className="card-text">Cena: {property.cena}</p>
                <p className="card-text">Prodaja/Izdaja: {property.prodajaIzdaja}</p>
                <p className="card-text">Tip: {property.tip}</p>
                <button className="btn btn-primary mr-2" onClick={() => handleLike(property.id)}>Like</button>
                <p className="card-text mt-2">Likes: {property.likes}</p>
                <button className="btn btn-secondary" onClick={() => redirectToProperty(property.id)}>
                  View Details
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default FilterComponent;
