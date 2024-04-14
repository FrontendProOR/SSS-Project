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

@RestController
public interface NekretninaRepozitorijum extends JpaRepository<Nekretnina, Long> {

    Nekretnina findById (int id);

    @Query(value = "SELECT * FROM nekretnine WHERE lokacija LIKE :lokacija AND povrsina LIKE :povrsina AND cena LIKE :cena AND prodaja_izdaja LIKE :prodaja AND tip LIKE :tip AND active = true", nativeQuery = true)
    List<Nekretnina> filter(@Param("lokacija") String lokacija, @Param("povrsina") String povrsina,
                            @Param("cena") String cena, @Param("prodaja") String prodaja, @Param("tip") String tip);

    @Query(value ="SELECT * FROM nekretnine WHERE korisnik_id IN :ids AND active = true", nativeQuery = true)
    List<Nekretnina> nekretnineAgencije(@Param("ids") List<Integer> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE nekretnine SET broj_pregleda = broj_pregleda + 1 WHERE id = :id", nativeQuery = true)
    void povecajBrojPregleda(@Param("id") int id);
}
