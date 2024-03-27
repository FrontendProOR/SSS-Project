package com.example.sss.servisi.implementacije;

import com.example.sss.model.DTO.KorisnikDTO;
import com.example.sss.model.Korisnik;
import com.example.sss.model.enumRole;
import com.example.sss.repozitorijumi.AgentRepozitorijum;
import com.example.sss.repozitorijumi.KorisnikRepozitorijum;
import com.example.sss.servisi.KorisnikServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class KorisnikServisImpl implements KorisnikServis {

    @Autowired
    private KorisnikRepozitorijum korisnikRepozitorijum;

    @Autowired
    private AgentRepozitorijum agentRepozitorijum;
    @Override
    public List<Korisnik> getAll() {
        System.out.println(korisnikRepozitorijum.findAll());
        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFF");
        return korisnikRepozitorijum.findAll();
    }

    @Override
    public Korisnik createUser(KorisnikDTO korisnikDTO) {

        Optional<Korisnik> korisnik = korisnikRepozitorijum.findFirstByEmail(korisnikDTO.getEmail());

        if(korisnik.isPresent()){
            return null;
        }
        korisnik = korisnikRepozitorijum.findFirstByNumTel(korisnikDTO.getBrojTelefona());

        if(korisnik.isPresent()){
            return null;
        }

        System.out.println("GUGUGGUGUGUGU");
        Korisnik novi = new Korisnik();
        novi.setEmail(korisnikDTO.getEmail());
        novi.setPassword(korisnikDTO.getPassword());
        novi.setFirstName(korisnikDTO.getFirstName());
        novi.setLastName(korisnikDTO.getLastName());
        novi.setNumTel(korisnikDTO.getBrojTelefona());
        novi.setAddress(korisnikDTO.getAdresa());
        System.out.println("ASHDASDASDHKJSADHA");
        novi.setRole(enumRole.valueOf(korisnikDTO.getUloga().toUpperCase()));
        novi.setActive(true);

        novi = korisnikRepozitorijum.save(novi);

        return novi;
    }

    public Korisnik createAgent(KorisnikDTO korisnikDTO, Korisnik vlasnik) {

        Optional<Korisnik> korisnik = korisnikRepozitorijum.findFirstByEmail(korisnikDTO.getEmail());

        if(korisnik.isPresent()){
            return null;
        }
        korisnik = korisnikRepozitorijum.findFirstByNumTel(korisnikDTO.getBrojTelefona());

        if(korisnik.isPresent()){
            return null;
        }

        System.out.println("GUGUGGUGUGUGU");
        Korisnik novi = new Korisnik();
        novi.setEmail(korisnikDTO.getEmail());
        novi.setPassword(korisnikDTO.getPassword());
        novi.setFirstName(korisnikDTO.getFirstName());
        novi.setLastName(korisnikDTO.getLastName());
        novi.setNumTel(korisnikDTO.getBrojTelefona());
        novi.setAddress(korisnikDTO.getAdresa());
        System.out.println("ASHDASDASDHKJSADHA");
        novi.setRole(enumRole.valueOf(korisnikDTO.getUloga().toUpperCase()));
        novi.setActive(true);

        novi = korisnikRepozitorijum.save(novi);
        System.out.println(novi.getId());
        System.out.println(vlasnik.getId());
        agentRepozitorijum.insert(novi.getId(), vlasnik.getId());
        System.out.println("]]]");

        return novi;
    }

}
