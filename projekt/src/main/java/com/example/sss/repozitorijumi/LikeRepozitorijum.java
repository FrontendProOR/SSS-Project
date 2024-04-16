package com.example.sss.repozitorijumi;

import com.example.sss.model.ImagePath;
import com.example.sss.model.Nekretnina;
import com.example.sss.model.Ocena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface LikeRepozitorijum extends JpaRepository<Ocena, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ocene (like_dislike, korisnik_id, nekretnina_id) VALUES (:likeDislike, :korisnikId, :nekretninaId)", nativeQuery = true)
    int insert(@Param("likeDislike") boolean like, @Param("korisnikId") int korisnikId, @Param("nekretninaId") int nekretninaId);

    @Query(value ="SELECT * FROM ocene WHERE korisnik_id = :kid AND nekretnina_id = :nid", nativeQuery = true)
    Ocena liked(@Param("kid") int kid, @Param("nid") int nid);

}
