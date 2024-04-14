package com.example.sss.servisi;

import com.example.sss.model.ImagePath;
import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;

import java.util.List;

public interface SlikaServis {

    List<ImagePath> getAll();

    List<ImagePath> getSlikeNekretnine(int id);

}
