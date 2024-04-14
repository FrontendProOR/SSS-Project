package com.example.sss.servisi.implementacije;

import com.example.sss.model.ImagePath;
import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;
import com.example.sss.repozitorijumi.KorisnikRepozitorijum;
import com.example.sss.repozitorijumi.NekretninaRepozitorijum;
import com.example.sss.repozitorijumi.SlikaRepozitorijum;
import com.example.sss.servisi.NekretninaServis;
import com.example.sss.servisi.SlikaServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlikaServisImpl implements SlikaServis {

    @Autowired
    private SlikaRepozitorijum slikaRepozitorijum;

    @Override
    public List<ImagePath> getAll() {
        System.out.println(slikaRepozitorijum.findAll());
        System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGG");
        return slikaRepozitorijum.findAll();
    }

    public List<ImagePath> getSlikeNekretnine (int id) {
        return slikaRepozitorijum.slikeNekretnine(id);
    }

}
