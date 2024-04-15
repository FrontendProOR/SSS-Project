package com.example.sss.servisi.implementacije;

import com.example.sss.model.*;
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

    /*public Nekretnina addNekretnina(String tip, double cena, String lokacija, double povrsina, String prodaja, int id) {
        Nekretnina novi = new Nekretnina();
        novi.setTip(enumTip.valueOf(tip));
        novi.setCena(cena);
        novi.setLokacija(lokacija);
        novi.setPovrsina(povrsina);
        novi.setProdajaIzdaja(enumProdajaIzdaja.valueOf(prodaja));
        novi.setId(id);
        System.out.println("ASHDASDASDHKJSADHA");
        novi.setActive(true);
        novi.setBrojPregleda(0);

        novi = nekretninaRepozitorijum.save(novi);

        return novi;
    }*/

}
