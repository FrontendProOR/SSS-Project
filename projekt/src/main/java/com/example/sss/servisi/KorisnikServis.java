package com.example.sss.servisi;

import com.example.sss.model.DTO.KorisnikDTO;
import com.example.sss.model.Korisnik;

import java.util.List;

public interface KorisnikServis {

    List<Korisnik> getAll();
    Korisnik createUser(KorisnikDTO korisnikDTO);

}
