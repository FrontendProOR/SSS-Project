package com.example.sss.servisi.implementacije;

import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;
import com.example.sss.repozitorijumi.KorisnikRepozitorijum;
import com.example.sss.repozitorijumi.NekretninaRepozitorijum;
import com.example.sss.servisi.NekretninaServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NekretninaServisImpl implements NekretninaServis {

    @Autowired
    private NekretninaRepozitorijum nekretninaRepozitorijum;

    @Override
    public List<Nekretnina> getAll() {
        System.out.println(nekretninaRepozitorijum.findAll());
        System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGG");
        return nekretninaRepozitorijum.findAll();
    }

}
