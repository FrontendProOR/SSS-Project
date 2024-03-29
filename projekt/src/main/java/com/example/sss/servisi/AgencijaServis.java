package com.example.sss.servisi;

import com.example.sss.model.Agencija;
import com.example.sss.model.DTO.AgencijaDTO;
import com.example.sss.model.Korisnik;

import java.util.List;

public interface AgencijaServis {
    List<Agencija> getAll();
    Agencija createAgencija(AgencijaDTO agencija, Korisnik vlasnik);
}
