package com.example.sss.kontroleri;

import com.example.sss.model.DTO.NekretninaDTO;
import com.example.sss.model.Nekretnina;
import com.example.sss.servisi.NekretninaServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/nekretnine")
@CrossOrigin(origins = "*")
public class NekretninaKontroler {

    @Autowired
    NekretninaServis nekretninaServis;

    @GetMapping("sve")
    public ResponseEntity<List<NekretninaDTO>> getAllNekretnine() {
        List<Nekretnina> nekretnine = nekretninaServis.getAll();
        List<NekretninaDTO> nekretnineDTOi = new ArrayList<>();

        for (Nekretnina nekretnina : nekretnine) {
            NekretninaDTO nekretninaDTO = new NekretninaDTO();
            nekretninaDTO.setLokacija(nekretnina.getLokacija());
            nekretninaDTO.setPovrsina(nekretnina.getPovrsina());
            nekretninaDTO.setCena(nekretnina.getCena());
            nekretninaDTO.setProdajaIzdavanje(nekretnina.getProdajaIzdavanje());
            nekretninaDTO.setTip(nekretnina.getTip());
            nekretninaDTO.setKorisnik(nekretnina.getKorisnik());
//            nekretninaDTO.setProdajaIzdavanje(String.valueOf(nekretnina.getProdajaIzdavanje()));
//            nekretninaDTO.setTip(String.valueOf(nekretnina.getTip()));
//            nekretninaDTO.setKorisnik(nekretnina.getKorisnik().getFirstName());
            System.out.println(nekretninaDTO.getCena());
            nekretnineDTOi.add(nekretninaDTO);
        }

        return ResponseEntity.ok(nekretnineDTOi);
    }

}
