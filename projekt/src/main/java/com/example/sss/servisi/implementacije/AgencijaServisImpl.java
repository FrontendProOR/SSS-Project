package com.example.sss.servisi.implementacije;

import com.example.sss.model.Agencija;
import com.example.sss.model.DTO.AgencijaDTO;
import com.example.sss.model.DTO.KorisnikDTO;
import com.example.sss.model.Korisnik;
import com.example.sss.repozitorijumi.AgencijaRepozitorijum;
import com.example.sss.repozitorijumi.KorisnikRepozitorijum;
import com.example.sss.servisi.AgencijaServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencijaServisImpl implements AgencijaServis {

    @Autowired
    private AgencijaRepozitorijum agencijaRepozitorijum;

    @Override
    public List<Agencija> getAll() {
        System.out.println(agencijaRepozitorijum.findAll());
        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFF");
        return agencijaRepozitorijum.findAll();
    }

    public Agencija createAgencija(AgencijaDTO agencija, Korisnik vlasnik) {
        Agencija nova = new Agencija();
        nova.setIme(agencija.getIme());
        nova.setOpis(agencija.getOpis());
        nova.setKorisnik(vlasnik);
        nova.setActive(true);

        nova = agencijaRepozitorijum.save(nova);

        return nova;
    }

}
