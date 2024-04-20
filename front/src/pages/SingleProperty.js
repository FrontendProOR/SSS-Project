import React from 'react';
import { useParams } from 'react-router-dom';
import PropertyDetails from './PropertyDetails';

function SingleNekretnina() {
  const { id } = useParams();

  return <PropertyDetails id={id} />;
}

export default SingleNekretnina;