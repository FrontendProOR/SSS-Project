package com.example.sss.kontroleri;

import com.example.sss.model.*;
import com.example.sss.model.DTO.KorisnikDTO;
import com.example.sss.model.DTO.NekretninaDTO;
import com.example.sss.model.DTO.TerminDTO;
import com.example.sss.repozitorijumi.KorisnikRepozitorijum;
import com.example.sss.repozitorijumi.NekretninaRepozitorijum;
import com.example.sss.repozitorijumi.TerminRepozitorijum;
import com.example.sss.servisi.KorisnikServis;
import com.example.sss.servisi.NekretninaServis;
import com.example.sss.servisi.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/nekretnine")
@CrossOrigin(origins = "*")
public class NekretninaKontroler {

    @Autowired
    NekretninaServis nekretninaServis;

    @Autowired
    NekretninaRepozitorijum nekretninaRepozitorijum;

    @Autowired
    KorisnikRepozitorijum korisnikRepozitorijum;

    @Autowired
    TerminRepozitorijum terminRepozitorijum;

    TokenUtils tokenUtils = new TokenUtils();

    @GetMapping("sve")
    public ResponseEntity<List<NekretninaDTO>> getAllNekretnine() {
        List<Nekretnina> nekretnine = nekretninaServis.getAll();
        List<NekretninaDTO> nekretnineDTOi = new ArrayList<>();

        for (Nekretnina nekretnina : nekretnine) {
            NekretninaDTO nekretninaDTO = new NekretninaDTO();
            nekretninaDTO.setId(nekretnina.getId());
            nekretninaDTO.setLokacija(nekretnina.getLokacija());
            nekretninaDTO.setPovrsina(nekretnina.getPovrsina());
            nekretninaDTO.setCena(nekretnina.getCena());
            nekretninaDTO.setProdajaIzdaja(String.valueOf(nekretnina.getProdajaIzdaja()));
            nekretninaDTO.setTip(String.valueOf(nekretnina.getTip()));
            nekretninaDTO.setKorisnik(nekretnina.getKorisnik().getFirstName());
            System.out.println(nekretninaDTO.getCena());
            nekretnineDTOi.add(nekretninaDTO);
        }

        return ResponseEntity.ok(nekretnineDTOi);
    }

    @GetMapping("/pretraga")
    public ResponseEntity<List<NekretninaDTO>> pretraga(
            @RequestParam(value = "lokacija", required = false) String lokacija,
            @RequestParam(value = "povrsina", required = false) String povrsina,
            @RequestParam(value = "cena", required = false) String cena,
            @RequestParam(value = "prodaja", required = false) String prodaja,
            @RequestParam(value = "tip", required = false) String tip) {

        if(lokacija == null || lokacija.isEmpty()) { lokacija = "%";}
        System.out.println(lokacija);

        if(povrsina == null || povrsina.isEmpty()) { povrsina = "%";}
        else {
            try {
                Double.parseDouble(povrsina);
            } catch (NumberFormatException e) {
                povrsina = "%";
            }
        }
        System.out.println(povrsina);

        if(cena == null || cena.isEmpty()) { cena = "%";}
        else {
            try {
                Double.parseDouble(cena);
            } catch (NumberFormatException e) {
                cena = "%";
            }
        }

        if(prodaja == null || prodaja.isEmpty()) { prodaja = "%";}
        else {
            try {
                enumProdajaIzdaja.valueOf(prodaja);
            } catch (IllegalArgumentException e) {
                prodaja = "%";
            }
        }

        if(tip == null || tip.isEmpty()) { tip = "%";}
        else {
            try {
                enumTip.valueOf(tip);
            } catch (IllegalArgumentException e) {
                tip = "%";
            }
        }

        System.out.println(lokacija + povrsina + cena + prodaja + tip);
        List<Nekretnina> nekretnine = nekretninaRepozitorijum.filter(lokacija, povrsina, cena, prodaja, tip);
        List<NekretninaDTO> nekretnineDTOi = new ArrayList<>();

        for (Nekretnina nekretnina : nekretnine) {
            NekretninaDTO nekretninaDTO = new NekretninaDTO();
            nekretninaDTO.setId(nekretnina.getId());
            nekretninaDTO.setLokacija(nekretnina.getLokacija());
            nekretninaDTO.setPovrsina(nekretnina.getPovrsina());
            nekretninaDTO.setCena(nekretnina.getCena());
            nekretninaDTO.setProdajaIzdaja(String.valueOf(nekretnina.getProdajaIzdaja()));
            nekretninaDTO.setTip(String.valueOf(nekretnina.getTip()));
            nekretninaDTO.setKorisnik(nekretnina.getKorisnik().getFirstName());
            System.out.println(nekretninaDTO.getCena());
            nekretnineDTOi.add(nekretninaDTO);
        }

        return ResponseEntity.ok(nekretnineDTOi);
    }

    @PostMapping("/zakazivanje")
    public ResponseEntity<TerminDTO> zakazivanje(@RequestBody @Validated TerminDTO terminDTO, @RequestHeader("authorization") String token){

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        }
        catch (Exception ignored){

        }

        if(email != null) {
            Korisnik korisnik = korisnikRepozitorijum.findByEmail(email);

            if (korisnik != null) {
                if (korisnik.getRole() == enumRole.KORISNIK) {
                    System.out.println(terminDTO.getDate());
                    System.out.println("you face jaraxxus");

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(terminDTO.getDate());
                    calendar.add(Calendar.HOUR_OF_DAY, -2);
                    calendar.add(Calendar.MILLISECOND, +1);
                    Date datum1 = calendar.getTime();
                    calendar.add(Calendar.HOUR_OF_DAY, +4);
                    calendar.add(Calendar.MILLISECOND, -2);
                    Date datum2 = calendar.getTime();

                    Termin termin = terminRepozitorijum.termin(datum1, datum2, korisnik.getId(), terminDTO.getId());

                    if(termin == null) {
                        System.out.println("you face jaraxxus");
                        terminRepozitorijum.insert(terminDTO.getDate(), korisnik.getId(), terminDTO.getId());
                        return new ResponseEntity<>(terminDTO, HttpStatus.CREATED);
                    }
                    return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NekretninaDTO> getNekrenina(@PathVariable String id) {

        Nekretnina nekretnina = nekretninaRepozitorijum.findById(Integer.parseInt(id));

        if(nekretnina != null) {
            NekretninaDTO nekretninaDTO = new NekretninaDTO();
            nekretninaDTO.setId(nekretnina.getId());
            nekretninaDTO.setLokacija(nekretnina.getLokacija());
            nekretninaDTO.setPovrsina(nekretnina.getPovrsina());
            nekretninaDTO.setCena(nekretnina.getCena());
            nekretninaDTO.setProdajaIzdaja(String.valueOf(nekretnina.getProdajaIzdaja()));
            nekretninaDTO.setTip(String.valueOf(nekretnina.getTip()));
            nekretninaDTO.setKorisnik(nekretnina.getKorisnik().getFirstName());
            System.out.println(nekretninaDTO.getCena());

            return new ResponseEntity<>(nekretninaDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
