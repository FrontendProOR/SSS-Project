package com.example.sss.repozitorijumi;

import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public interface KorisnikRepozitorijum extends JpaRepository<Korisnik, Long> {

    Korisnik findByEmail(String email);
    Optional<Korisnik> findFirstByEmail(String email);
    Optional<Korisnik> findFirstByNumTel(String numtel);

    @Query(value ="SELECT * FROM korisnici WHERE id = :id AND active = true", nativeQuery = true)
    Korisnik nadjiKorisnika(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE korisnici SET active = false WHERE id = :id", nativeQuery = true)
    void obrisi(@Param("id") int id);
}
