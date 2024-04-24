package com.example.sss.repozitorijumi;

import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;
import com.example.sss.model.enumProdajaIzdaja;
import com.example.sss.model.enumTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface NekretninaRepozitorijum extends JpaRepository<Nekretnina, Long> {

    Nekretnina findById (int id);

    @Query(value = "SELECT * FROM nekretnine WHERE lokacija LIKE :lokacija AND povrsina BETWEEN :povrsinamin AND :povrsinamax AND cena BETWEEN :cenamin AND :cenamax AND prodaja_izdaja LIKE :prodaja AND tip LIKE :tip AND active = true", nativeQuery = true)
    List<Nekretnina> filter(@Param("lokacija") String lokacija, @Param("povrsinamin") String povrsinamin, @Param("povrsinamax") String povrsinamax,
                            @Param("cenamin") String cenamin, @Param("cenamax") String cenamax, @Param("prodaja") String prodaja, @Param("tip") String tip);

    @Query(value ="SELECT * FROM nekretnine WHERE korisnik_id IN :ids AND active = true", nativeQuery = true)
    List<Nekretnina> nekretnineAgencije(@Param("ids") List<Integer> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE nekretnine SET broj_pregleda = broj_pregleda + 1 WHERE id = :id", nativeQuery = true)
    void povecajBrojPregleda(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO nekretnine (tip, cena, lokacija, povrsina, prodaja_izdaja, korisnik_id, active, broj_pregleda) VALUES (:tip, :cena, :lokacija, :povrsina, :prodaja, :id, true, 0)", nativeQuery = true)
    int insert(@Param("tip") String tip, @Param("cena") double cena, @Param("lokacija") String lokacija, @Param("povrsina") double povrsina, @Param("prodaja") String prodaja, @Param("id") int id);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    int getLastInsertedId();

    @Query(value ="SELECT * FROM nekretnine WHERE korisnik_id = :id AND active = true", nativeQuery = true)
    List<Nekretnina> nekretnineAgenta(@Param("id") int id);

    @Query(value ="SELECT * FROM nekretnine WHERE korisnik_id = :id", nativeQuery = true)
    List<Nekretnina> sveNekretnineAgenta(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE nekretnine SET active = false WHERE id = :id", nativeQuery = true)
    void obrisi(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE nekretnine SET cena = :cena, prodaja_izdaja = :prodaja WHERE id = :id", nativeQuery = true)
    void izmeni(@Param("cena") double cena, @Param("prodaja") String prodaja, @Param("id") int id);

    @Query(value ="SELECT * FROM nekretnine WHERE korisnik_id = :kid AND nekretnina_id = :nid", nativeQuery = true)
    Nekretnina odrediVlasnistvo(@Param("kid") int kid, @Param("nid") int nid);
}
