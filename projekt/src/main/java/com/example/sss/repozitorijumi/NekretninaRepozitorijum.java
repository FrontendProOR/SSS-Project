package com.example.sss.repozitorijumi;

import com.example.sss.model.Nekretnina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface NekretninaRepozitorijum extends JpaRepository<Nekretnina, Long> {

    Nekretnina findById (Nekretnina nekretnina);

}
