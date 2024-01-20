package com.example.sss.kontroleri;

import com.example.sss.servisi.TokenUtils;
import com.example.sss.model.DTO.KorisnickiToken;
import com.example.sss.model.DTO.KorisnikDTO;
import com.example.sss.model.DTO.Kredencijali;
import com.example.sss.model.Korisnik;
import com.example.sss.repozitorijumi.KorisnikRepozitorijum;
import com.example.sss.servisi.KorisnikServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("api/korisnici")
@CrossOrigin(origins = "*")
public class KorisnikKontroler {

    @Autowired
    KorisnikServis korisnikServis;

    @Autowired
    KorisnikRepozitorijum korisnikRepozitorijum;

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

    @PostMapping("prijava")
    public ResponseEntity<KorisnickiToken> generateToken(@RequestBody Kredencijali kredencijali) {

        Korisnik korisnik = korisnikRepozitorijum.findByEmail(kredencijali.getEmail());

        if(korisnik != null){
            String token = tokenUtils.generateJwtToken(korisnik);
            return ResponseEntity.ok(new KorisnickiToken(token));
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
