package com.example.sss.kontroleri;

import com.example.sss.model.*;
import com.example.sss.model.DTO.KorisnikDTO;
import com.example.sss.model.DTO.LikeDTO;
import com.example.sss.model.DTO.NekretninaDTO;
import com.example.sss.model.DTO.TerminDTO;
import com.example.sss.repozitorijumi.*;
import com.example.sss.servisi.ImageUtils;
import com.example.sss.servisi.KorisnikServis;
import com.example.sss.servisi.NekretninaServis;
import com.example.sss.servisi.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
    AgentRepozitorijum agentRepozitorijum;

    @Autowired
    TerminRepozitorijum terminRepozitorijum;

    @Autowired
    SlikaRepozitorijum slikaRepozitorijum;

    @Autowired
    LikeRepozitorijum likeRepozitorijum;

    TokenUtils tokenUtils = new TokenUtils();

    ImageUtils imageUtils = new ImageUtils();

    @Value("${image.directory.path}")
    private String putanja;

    @GetMapping("/pretraga")
    public ResponseEntity<List<NekretninaDTO>> pretraga(
            @RequestParam(value = "lokacija", required = false) String lokacija,
            @RequestParam(value = "povrsina", required = false) String povrsina,
            @RequestParam(value = "cena", required = false) String cena,
            @RequestParam(value = "prodaja", required = false) String prodaja,
            @RequestParam(value = "tip", required = false) String tip,
            @RequestHeader("authorization") String token) {

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

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        }
        catch (Exception ignored){

        }
        Korisnik korisnik = null;

        if(email != null) {
            korisnik = korisnikRepozitorijum.findByEmail(email);
        }

        System.out.println(lokacija + povrsina + cena + prodaja + tip);
        List<Nekretnina> nekretnine = nekretninaRepozitorijum.filter(lokacija, povrsina, cena, prodaja, tip);
        Collections.reverse(nekretnine);
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
            nekretninaDTO.setBrojPregleda(nekretnina.getBrojPregleda());
            System.out.println(nekretninaDTO.getCena());

            List<ImagePath> slikeUBase64 = slikaRepozitorijum.slikeNekretnine(nekretnina.getId());
            for(ImagePath slika : slikeUBase64){
                System.out.println(slika.getImagePath());
            }
            List<String> imenafajlova = slikeUBase64.stream()
                    .map(ImagePath::getImagePath)
                    .collect(Collectors.toList());
            for(String slika : imenafajlova){
                System.out.println(slika);
            }

            List<String> potpuneputanje = new ArrayList<>();
            for(String slika : imenafajlova){
                potpuneputanje.add(putanja + slika);
            }

            for(String slika : potpuneputanje){
                System.out.println(slika);
            }

            List<String> enkodiraneSlike = new ArrayList<>();
            for(String slika : potpuneputanje){
                File putanja = new File(slika);
                try {
                    byte[] imageData = Files.readAllBytes(putanja.toPath());
                    String base64Slika = Base64.getEncoder().encodeToString(imageData);
                    enkodiraneSlike.add(base64Slika);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }

            nekretninaDTO.setSlikeUBase64(enkodiraneSlike);
            if (korisnik != null) {
                Ocena ocena = likeRepozitorijum.liked(korisnik.getId(), nekretnina.getId());
                System.out.println(korisnik.getId() + "bruhimics" +  nekretnina.getId());
                if (ocena != null) {
                    System.out.println(ocena.likeDislike);
                    nekretninaDTO.setLiked(ocena.likeDislike);
                }
            };
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
                    Nekretnina nekretnina = nekretninaRepozitorijum.findById(terminDTO.getId());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(terminDTO.getDate());
                    calendar.add(Calendar.HOUR_OF_DAY, -2);
                    calendar.add(Calendar.MILLISECOND, +1);
                    Date datum1 = calendar.getTime();
                    calendar.add(Calendar.HOUR_OF_DAY, +4);
                    calendar.add(Calendar.MILLISECOND, -2);
                    Date datum2 = calendar.getTime();

                    if (nekretnina != null && nekretnina.isActive()) {
                        Termin termin = terminRepozitorijum.termin(datum1, datum2, korisnik.getId(), terminDTO.getId());

                        if (termin == null) {
                            System.out.println("you face jaraxxus");
                            terminRepozitorijum.insert(terminDTO.getDate(), korisnik.getId(), terminDTO.getId());
                            return new ResponseEntity<>(terminDTO, HttpStatus.CREATED);
                        }
                    }
                    return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NekretninaDTO> getNekrenina(@PathVariable String id, @RequestHeader("authorization") String token) {

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        }
        catch (Exception ignored){

        }
        Korisnik korisnik = null;

        if(email != null) {
            korisnik = korisnikRepozitorijum.findByEmail(email);
        }

        Nekretnina nekretnina = nekretninaRepozitorijum.findById(Integer.parseInt(id));

        if(nekretnina != null) {
            if (nekretnina.isActive()) {
                NekretninaDTO nekretninaDTO = new NekretninaDTO();
                nekretninaDTO.setId(nekretnina.getId());
                nekretninaDTO.setLokacija(nekretnina.getLokacija());
                nekretninaDTO.setPovrsina(nekretnina.getPovrsina());
                nekretninaDTO.setCena(nekretnina.getCena());
                nekretninaDTO.setProdajaIzdaja(String.valueOf(nekretnina.getProdajaIzdaja()));
                nekretninaDTO.setTip(String.valueOf(nekretnina.getTip()));
                nekretninaDTO.setKorisnik(nekretnina.getKorisnik().getFirstName());
                nekretninaDTO.setBrojPregleda(nekretnina.getBrojPregleda());
                System.out.println(nekretninaDTO.getCena());

                List<ImagePath> slikeUBase64 = slikaRepozitorijum.slikeNekretnine(nekretnina.getId());
                for (ImagePath slika : slikeUBase64) {
                    System.out.println(slika.getImagePath());
                }
                List<String> imenafajlova = slikeUBase64.stream()
                        .map(ImagePath::getImagePath)
                        .collect(Collectors.toList());
                for (String slika : imenafajlova) {
                    System.out.println(slika);
                }

                List<String> potpuneputanje = new ArrayList<>();
                for (String slika : imenafajlova) {
                    potpuneputanje.add(putanja + slika);
                }

                for (String slika : potpuneputanje) {
                    System.out.println(slika);
                }

                List<String> enkodiraneSlike = new ArrayList<>();
                for (String slika : potpuneputanje) {
                    File putanja = new File(slika);
                    try {
                        byte[] imageData = Files.readAllBytes(putanja.toPath());
                        String base64Slika = Base64.getEncoder().encodeToString(imageData);
                        enkodiraneSlike.add(base64Slika);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                nekretninaDTO.setSlikeUBase64(enkodiraneSlike);
                //System.out.println(nekretninaDTO.getSlikeUBase64());

                if (korisnik != null) {
                    Ocena ocena = likeRepozitorijum.liked(korisnik.getId(), nekretnina.getId());
                    System.out.println(korisnik.getId() + "bruhimics" +  nekretnina.getId());
                    if (ocena != null) {
                        System.out.println(ocena.likeDislike);
                        nekretninaDTO.setLiked(ocena.likeDislike);
                    }
                };

                nekretninaRepozitorijum.povecajBrojPregleda(nekretnina.getId());

                return new ResponseEntity<>(nekretninaDTO, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/novanekretnina")
    public ResponseEntity<NekretninaDTO> createNekretnina(@RequestBody @Validated NekretninaDTO nekretninaDTO, @RequestHeader("authorization") String token) {

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
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {

                    try {
                        enumTip.valueOf(nekretninaDTO.tip);
                    }
                    catch (Exception e) {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                    try {
                        enumProdajaIzdaja.valueOf(nekretninaDTO.prodajaIzdaja);
                    }
                    catch (Exception e) {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }

                    nekretninaRepozitorijum.insert(nekretninaDTO.tip, nekretninaDTO.cena, nekretninaDTO.lokacija, nekretninaDTO.povrsina, nekretninaDTO.prodajaIzdaja, korisnik.getId());
                    int id = nekretninaRepozitorijum.getLastInsertedId();

                    for(String suiseiseki : nekretninaDTO.slikeUBase64) {
                        try {
                            byte[] imageBytes = Base64.getDecoder().decode(suiseiseki);

                            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                            StringBuilder sb = new StringBuilder(25);
                            Random random = new Random();
                            for (int i = 0; i < 25; i++) {
                                int randomIndex = random.nextInt(characters.length());
                                char randomChar = characters.charAt(randomIndex);
                                sb.append(randomChar);
                            }

                            FileOutputStream outputStream = new FileOutputStream(putanja + sb.toString() + ".jpg");
                            outputStream.write(imageBytes);
                            outputStream.close();

                            slikaRepozitorijum.insert(sb.toString() + ".jpg", id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/mojaagencija")
    public ResponseEntity<List<NekretninaDTO>> getNekrenineMojeAgencije(@RequestHeader("authorization") String token) {

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
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {
                    List<Agent> agentiPodVlasnikom;
                    if (korisnik.getRole() == enumRole.AGENT) {
                        Agent vlasnik = agentRepozitorijum.nadjivlasnika(korisnik.getId());
                        System.out.println(vlasnik.getAgent() + "pipan" + vlasnik.getVlasnik() + "BRUHIMICS");
                        agentiPodVlasnikom = agentRepozitorijum.nadjiSveAgentePodVlasnikom(vlasnik.getVlasnik());
                    }
                    else {
                        agentiPodVlasnikom = agentRepozitorijum.nadjiSveAgentePodVlasnikom(korisnik.getId());
                    }
                    for(Agent agent : agentiPodVlasnikom){
                        System.out.println(agent.getAgent());
                    }
                    List<Integer> agentIds = agentiPodVlasnikom.stream()
                            .map(Agent::getAgent)
                            .collect(Collectors.toList());
                    for(Integer ints : agentIds){
                        System.out.println(ints);
                    }
                    List<Nekretnina> nekretnine = nekretninaRepozitorijum.nekretnineAgencije(agentIds);
                    for(Nekretnina nekretnina : nekretnine) {
                        System.out.println(nekretnina.getId());
                    }
                    Collections.reverse(nekretnine);
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
                        nekretninaDTO.setBrojPregleda(nekretninaDTO.getBrojPregleda());
                        System.out.println(nekretninaDTO.getCena());
                        nekretnineDTOi.add(nekretninaDTO);
                    }

                    return ResponseEntity.ok(nekretnineDTOi);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/like")
    public ResponseEntity<List<NekretninaDTO>> like(@RequestBody @Validated LikeDTO likeDTO, @RequestHeader("authorization") String token) {

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        } catch (Exception ignored) {

        }

        if (email != null) {
            Korisnik korisnik = korisnikRepozitorijum.findByEmail(email);

            if (korisnik != null) {
                if (korisnik.getRole() == enumRole.KORISNIK) {
                    Nekretnina nekretnina = nekretninaRepozitorijum.findById(likeDTO.nekretninaId);

                    if (nekretnina != null) {
                        Ocena ocena = likeRepozitorijum.liked(korisnik.getId(), likeDTO.nekretninaId);
                        System.out.println("ukakicu se");
                        System.out.println(korisnik.getId() + "niger" +  likeDTO.nekretninaId);

                        if (ocena == null) {
                            likeRepozitorijum.insert(likeDTO.like, korisnik.getId(), likeDTO.nekretninaId);
                            return new ResponseEntity<>(null, HttpStatus.OK);
                        }
                    }

                    return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/mojitermini")
    public ResponseEntity<List<TerminDTO>> getMojiTermini(@RequestHeader("authorization") String token) {

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        } catch (Exception ignored) {

        }

        if (email != null) {
            Korisnik korisnik = korisnikRepozitorijum.findByEmail(email);

            if (korisnik != null) {
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {
                    List<Nekretnina> nekretnine = nekretninaRepozitorijum.nekretnineAgenta(korisnik.getId());

                    List<Integer> ids = nekretnine.stream()
                            .map(Nekretnina::getId)
                            .collect(Collectors.toList());
                    List<Termin> termini = terminRepozitorijum.terminiAgenta(ids);
                    List<TerminDTO> terminDTOi = new ArrayList<>();
                    for (Termin termin : termini) {
                        TerminDTO terminDTO = new TerminDTO();
                        terminDTO.setIdTermina(termin.getId());
                        terminDTO.setDate(termin.getDatum());
                        terminDTO.setId(termin.getNekretnina().getId());
                        terminDTOi.add(terminDTO);
                    }

                    return ResponseEntity.ok(terminDTOi);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/prihvatizahtev")
    public ResponseEntity<TerminDTO> prihvatiZahtev(@RequestBody @Validated TerminDTO terminDTO, @RequestHeader("authorization") String token) {

        for (int m = 0; m < 10; m++) {
            System.out.println("!!!!!!!!!!!!!!!!");
        }

        String email = null;
        try {
            email = tokenUtils.getClaimsFromToken(token).getSubject();
        } catch (Exception ignored) {

        }

        if (email != null) {
            Korisnik korisnik = korisnikRepozitorijum.findByEmail(email);

            if (korisnik != null) {
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {
                    Termin termin = terminRepozitorijum.nadji(terminDTO.getIdTermina());
                }
            }
        }
        return null;
    }
}
