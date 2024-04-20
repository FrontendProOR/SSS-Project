package com.example.sss.repozitorijumi;

import com.example.sss.model.Agencija;
import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;
import com.example.sss.model.Transakcija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface AgencijaRepozitorijum extends JpaRepository<Agencija, Long> {
    Agencija findFirstByIme(String ime);

    @Query(value = "SELECT * FROM agencije WHERE korisnik_id = :nid", nativeQuery = true)
    Agencija nadji(@Param("nid") int nid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE agencije SET ime = :ime, opis = :opis WHERE id = :id", nativeQuery = true)
    void izmeni(@Param("ime") String ime, @Param("opis") String opis, @Param("id") int id);
}
