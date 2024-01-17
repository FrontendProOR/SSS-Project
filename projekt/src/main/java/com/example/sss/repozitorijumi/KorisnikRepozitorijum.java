package com.example.sss.repozitorijumi;

import com.example.sss.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface KorisnikRepozitorijum extends JpaRepository<Korisnik, Long> {

    Korisnik findByEmail(Korisnik korisnik);
    Optional<Korisnik> findFirstByEmail(String email);
}
