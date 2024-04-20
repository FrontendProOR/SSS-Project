import React, { useState, useEffect, useParams } from 'react';
import axios from 'axios';

import PropertyDetails from './PropertyDetails';

const PropertyDetails = ({ id }) => {
  const [nekretnina, setNekretnina] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProperty = async () => {
      try {
        const token = 'token'; 
        const response = await axios.get(`http://localhost:8080/api/nekretnine/${id}`, {
          headers: {
            authorization: token,
          },
        });
        setNekretnina(response.data);
        setLoading(false);
      } catch (error) {
        setError(error);
        setLoading(false);
      }
    };

    fetchProperty();
  }, [id]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  return (
    <div className="container mt-4">
      <h2>Detalji o nekretnini</h2>
      <div className="card">
        <div className="card-body">
          <h5 className="card-title">ID: {nekretnina.id}</h5>
          <p className="card-text">Lokacija: {nekretnina.lokacija}</p>
          <p className="card-text">Površina: {nekretnina.povrsina}</p>
          <p className="card-text">Cena: {nekretnina.cena}</p>
          <p className="card-text">Prodaja/Izdaja: {nekretnina.prodajaIzdaja}</p>
          <p className="card-text">Tip: {nekretnina.tip}</p>
          <p className="card-text">Korisnik: {nekretnina.korisnik}</p>
          <p className="card-text">Broj Pregleda: {nekretnina.brojPregleda}</p>
          <p className="card-text">Liked: {nekretnina.liked ? 'Da' : 'Ne'}</p>
          <h5>Images:</h5>
          <div className="row">
            {nekretnina.slikeUBase64.map((image, index) => (
              <div key={index} className="col-md-3 mb-2">
                <img src={`data:image/jpeg;base64,${image}`} alt={`Image ${index}`} className="img-fluid" />
              </div>
            ))}
          </div>
          <h5>Termini:</h5>
          <ul className="list-group">
            {nekretnina.termini.map((termin, index) => (
              <li key={index} className="list-group-item">{termin.date}</li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default PropertyDetails;
