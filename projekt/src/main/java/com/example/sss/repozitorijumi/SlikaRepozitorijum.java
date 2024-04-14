package com.example.sss.repozitorijumi;

import com.example.sss.model.ImagePath;
import com.example.sss.model.Nekretnina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface SlikaRepozitorijum extends JpaRepository<ImagePath, Long> {

    @Query(value ="SELECT * FROM slike WHERE nekretnina_id = :id", nativeQuery = true)
    List<ImagePath> slikeNekretnine(@Param("id") int id);
}
