package com.example.sss.repozitorijumi;

import com.example.sss.model.Recenzija;
import com.example.sss.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RecenzijaRepozitorijum extends JpaRepository<Recenzija, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO recenzije (korisnik_id, agent_id, ocena, opis) VALUES (:kid, :aid, :ocena, :opis)", nativeQuery = true)
    int insert(@Param("kid") int kid, @Param("aid") int aid, @Param("ocena") int ocena, @Param("opis") String opis);

    @Query(value ="SELECT * FROM recenzije WHERE agent_id IN :ids", nativeQuery = true)
    List<Recenzija> recenzijeAgenataAgencije(@Param("ids") List<Integer> ids);

    @Query(value ="SELECT * FROM recenzije WHERE agent_id = :id", nativeQuery = true)
    List<Recenzija> recenzijeAgenta(@Param("id") int id);
}
