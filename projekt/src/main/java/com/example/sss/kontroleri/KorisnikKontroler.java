package com.example.sss.kontroleri;

import com.example.sss.model.Agencija;
import com.example.sss.model.DTO.*;
import com.example.sss.model.enumRole;
import com.example.sss.repozitorijumi.AgencijaRepozitorijum;
import com.example.sss.servisi.AgencijaServis;
import com.example.sss.servisi.TokenUtils;
import com.example.sss.model.Korisnik;
import com.example.sss.repozitorijumi.KorisnikRepozitorijum;
import com.example.sss.servisi.KorisnikServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;


@RestController
@RequestMapping("api/korisnici")
@CrossOrigin(origins = "*")
public class KorisnikKontroler {

    @Autowired
    KorisnikServis korisnikServis;

    @Autowired
    AgencijaServis agencijaServis;

    @Autowired
    KorisnikRepozitorijum korisnikRepozitorijum;

    @Autowired
    AgencijaRepozitorijum agencijaRepozitorijum;

    TokenUtils tokenUtils = new TokenUtils();

    @PostMapping("registracija")
    public ResponseEntity<KorisnikDTO> create(@RequestBody @Validated KorisnikDTO noviKorisnik){

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        System.out.println(noviKorisnik.getEmail() + noviKorisnik.getUloga() + noviKorisnik.getAdresa());
        noviKorisnik.setUloga("KORISNIK");
        Korisnik kreirani = korisnikServis.createUser(noviKorisnik);
        System.out.println(kreirani);

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        if(kreirani == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        KorisnikDTO korisnikDTO = new KorisnikDTO(kreirani);

        return new ResponseEntity<>(korisnikDTO, HttpStatus.CREATED);
    }

    @PostMapping("registracijaagenta")
    public ResponseEntity<KorisnikDTO> createagent(@RequestBody @Validated KorisnikDTO noviKorisnik, @RequestHeader("authorization") String token){

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        }
        catch (Exception ignored){

        }

        if(email != null) {
            Korisnik korisnik = korisnikRepozitorijum.findByEmail(email);

            if (korisnik != null) {
                if (korisnik.getRole() == enumRole.VLASNIK) {
                    System.out.println(noviKorisnik.getEmail() + noviKorisnik.getUloga() + noviKorisnik.getAdresa());
                    noviKorisnik.setUloga("AGENT");
                    //sussy baka
                    Korisnik kreirani = korisnikServis.createAgent(noviKorisnik, korisnik);
                    System.out.println(kreirani);

                    for (int m = 0; m < 10; m++) {
                        System.out.println(";;;;;;;;;;;;;;;;");
                    }

                    if (kreirani == null) {
                        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                    }
                    KorisnikDTO korisnikDTO = new KorisnikDTO(kreirani);

                    return new ResponseEntity<>(korisnikDTO, HttpStatus.CREATED);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

    }

    @PostMapping("registracijavlasnika")
    public ResponseEntity<VlasnikAgencija> createvlasnik(@RequestBody VlasnikAgencija vlasnikAgencija, @RequestHeader("authorization") String token){

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        }
        catch (Exception ignored){

        }

        if(email != null) {
            Korisnik korisnik = korisnikRepozitorijum.findByEmail(email);
            System.out.println("ZZZZZZZZZZZZZZZZZ");

            if (korisnik != null) {
                System.out.println("NEMTETSZIKSEMMIMELO");
                if (korisnik.getRole() == enumRole.ADMIN) {

                    KorisnikDTO noviKorisnik = new KorisnikDTO();
                    noviKorisnik.setEmail(vlasnikAgencija.email);
                    noviKorisnik.setPassword(vlasnikAgencija.password);
                    noviKorisnik.setFirstName(vlasnikAgencija.firstName);
                    noviKorisnik.setLastName(vlasnikAgencija.lastName);
                    noviKorisnik.setBrojTelefona(vlasnikAgencija.brojTelefona);
                    noviKorisnik.setAdresa(vlasnikAgencija.adresa);

                    AgencijaDTO agencija = new AgencijaDTO();
                    agencija.setIme(vlasnikAgencija.getIme());
                    agencija.setOpis(vlasnikAgencija.getOpis());

                    System.out.println(noviKorisnik.getEmail() + noviKorisnik.getUloga() + noviKorisnik.getAdresa());
                    noviKorisnik.setUloga("VLASNIK");

                    if(agencijaRepozitorijum.findFirstByIme(agencija.getIme()) != null) {
                        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                    }

                    Korisnik vlasnik = new Korisnik();

                    Korisnik kreirani = korisnikServis.createAgent(noviKorisnik, vlasnik);
                    System.out.println(kreirani);

                    for (int m = 0; m < 10; m++) {
                        System.out.println(";;;;;;;;;;;;;;;;");
                    }

                    if (kreirani == null) {
                        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                    }

                    agencijaServis.createAgencija(agencija, kreirani);

                    VlasnikAgencija vlasnikAgencijaNova = new VlasnikAgencija();
                    vlasnikAgencijaNova.setEmail(noviKorisnik.email);
                    vlasnikAgencijaNova.setFirstName(noviKorisnik.firstName);
                    vlasnikAgencijaNova.setLastName(noviKorisnik.lastName);
                    vlasnikAgencijaNova.setBrojTelefona(noviKorisnik.brojTelefona);
                    vlasnikAgencijaNova.setAdresa(noviKorisnik.adresa);
                    vlasnikAgencijaNova.setUloga(noviKorisnik.uloga);
                    vlasnikAgencijaNova.setIme(agencija.getIme());
                    vlasnikAgencijaNova.setOpis(agencija.getOpis());

                    return new ResponseEntity<>(vlasnikAgencijaNova, HttpStatus.CREATED);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

    }

    @PostMapping("registracijaadmina")
    public ResponseEntity<KorisnikDTO> createadmin(@RequestBody @Validated KorisnikDTO noviKorisnik, @RequestHeader("authorization") String token){

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        }
        catch (Exception ignored){

        }

        if(email != null) {
            Korisnik korisnik = korisnikRepozitorijum.findByEmail(email);

            if (korisnik != null) {
                if (korisnik.getRole() == enumRole.ADMIN) {
                    System.out.println(noviKorisnik.getEmail() + noviKorisnik.getUloga() + noviKorisnik.getAdresa());
                    noviKorisnik.setUloga("ADMIN");
                    Korisnik kreirani = korisnikServis.createUser(noviKorisnik);
                    System.out.println(kreirani);

                    for (int m = 0; m < 10; m++) {
                        System.out.println(";;;;;;;;;;;;;;;;");
                    }

                    if (kreirani == null) {
                        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                    }
                    KorisnikDTO korisnikDTO = new KorisnikDTO(kreirani);

                    return new ResponseEntity<>(korisnikDTO, HttpStatus.CREATED);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

    }

    @PostMapping("prijava")
    public ResponseEntity<KorisnickiToken> generateToken(@RequestBody Kredencijali kredencijali) {

        Korisnik korisnik = korisnikRepozitorijum.findByEmail(kredencijali.getEmail());

        if(korisnik != null){
            if (korisnik.isActive()) {
                if (kredencijali.getPassword().equals(korisnik.getPassword())) {
                    String token = tokenUtils.generateJwtToken(korisnik);
                    return ResponseEntity.ok(new KorisnickiToken(token, String.valueOf(korisnik.getRole())));
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
