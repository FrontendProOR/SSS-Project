package com.example.sss.kontroleri;

import com.example.sss.model.DTO.KorisnikDTO;
import com.example.sss.model.Korisnik;
import com.example.sss.servisi.KorisnikServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/korisnici")
@CrossOrigin(origins = "*")
public class KorisnikKontroler {

    @Autowired
    KorisnikServis korisnikServis;

    @PostMapping("registracija")
    public ResponseEntity<KorisnikDTO> create(@RequestBody @Validated KorisnikDTO noviKorisnik){

        for (int m = 0; m < 10; m++) {
            System.out.println("Hello world registracija");
        }
        System.out.println(noviKorisnik.getEmail() + noviKorisnik.getUloga() + noviKorisnik.getAdresa());
        Korisnik kreirani = korisnikServis.createUser(noviKorisnik);
        System.out.println(kreirani);

        for (int m = 0; m < 10; m++) {
            System.out.println("Hello world registracija");
        }

        if(kreirani == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        KorisnikDTO korisnikDTO = new KorisnikDTO(kreirani);

        return new ResponseEntity<>(korisnikDTO, HttpStatus.CREATED);
    }
}
