package com.example.sss.repozitorijumi;

import com.example.sss.model.Termin;
import com.example.sss.model.Transakcija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface TransakcijaRepozitorijum extends JpaRepository<Transakcija, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO transakcije (nekretnina_id, timestamp) VALUES (:nid, :timestamp)", nativeQuery = true)
    int insert(@Param("nid") int nid, @Param("timestamp") Date timestamp);

    @Query(value = "SELECT * FROM transakcije WHERE nekretnina_id = :nid", nativeQuery = true)
    Transakcija nadji(@Param("nid") int nid);

    @Query(value = "SELECT * FROM transakcije WHERE YEAR(timestamp) = :godina AND MONTH(timestamp) = :mesec", nativeQuery = true)
    List<Transakcija> transakcijeZaMesec(@Param("godina") int godina, @Param("mesec") int mesec);
}
