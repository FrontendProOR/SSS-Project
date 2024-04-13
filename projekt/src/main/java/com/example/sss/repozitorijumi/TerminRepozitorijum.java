package com.example.sss.repozitorijumi;

import com.example.sss.model.Agent;
import com.example.sss.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface TerminRepozitorijum extends JpaRepository<Termin, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO termini (datum, korisnik_id, nekretnina_id, active) VALUES (:datum, :korisnik_id, :nekretnina_id, true)", nativeQuery = true)
    int insert(@Param("datum") Date datum, @Param("korisnik_id") int korisnik_id, @Param("nekretnina_id") int nekretnina_id);

    @Query(value = "SELECT * FROM termini WHERE (korisnik_id = :korisnik_id AND nekretnina_id = :nekretnina_id AND active = true) OR (datum BETWEEN :datum1 AND :datum2 AND nekretnina_id = :nekretnina_id AND active = true) LIMIT 1;", nativeQuery = true)
    Termin termin(@Param("datum1") Date datum1, @Param("datum2") Date datum2, @Param("korisnik_id") int korisnik_id, @Param("nekretnina_id") int nekretnina_id);
}
