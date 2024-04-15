package com.example.sss.servisi;

import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;

import java.util.List;

public interface NekretninaServis {

    List<Nekretnina> getAll();

   // Nekretnina addNekretnina(String tip, double cena, String lokacija, double povrsina, String prodaja, int id);

}
