import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);

  useEffect(() => {
    const handleOutsideClick = (e) => {
      if (isDropdownOpen && !e.target.closest('.dropdown')) {
        setIsDropdownOpen(false);
      }
    };

    document.body.addEventListener('click', handleOutsideClick);

    return () => {
      document.body.removeEventListener('click', handleOutsideClick);
    };
  }, [isDropdownOpen]);

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container">
        <Link className="navbar-brand" to="/">Home</Link>
        <button className="navbar-toggler" type="button" onClick={toggleDropdown}>
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className={`collapse navbar-collapse ${isDropdownOpen ? 'show' : ''}`} id="navbarSupportedContent">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <Link className="nav-link" to="/login">Login</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/nekretnina/create">Nova nekretnina</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/nekretnina">Signle nekretnina</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/pretraga">Pretraga</Link>
            </li>
            <li className="nav-item dropdown">
              <a className="nav-link dropdown-toggle" href="#" role="button" onClick={toggleDropdown}>
                Register
              </a>
              <ul className={`dropdown-menu ${isDropdownOpen ? 'show' : ''}`} aria-labelledby="navbarDropdown">
                <li><Link className="dropdown-item" to="/register/korisnik">Korisnik</Link></li>
                <li><Link className="dropdown-item" to="/register/agent">Agent</Link></li>
                <li><Link className="dropdown-item" to="/register/admin">Admin</Link></li>
                <li><Link className="dropdown-item" to="/register/vlasnik">Vlasnik</Link></li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
