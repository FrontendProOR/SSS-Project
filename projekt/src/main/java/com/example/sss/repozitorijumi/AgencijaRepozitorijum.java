package com.example.sss.repozitorijumi;

import com.example.sss.model.Agencija;
import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface AgencijaRepozitorijum extends JpaRepository<Agencija, Long> {
    Agencija findFirstByIme(String ime);
}
