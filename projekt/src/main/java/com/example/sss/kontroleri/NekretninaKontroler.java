package com.example.sss.kontroleri;

import com.example.sss.model.*;
import com.example.sss.model.DTO.*;
import com.example.sss.model.IzvestajDTO;
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
    RecenzijaRepozitorijum recenzijaRepozitorijum;

    @Autowired
    TransakcijaRepozitorijum transakcijaRepozitorijum;

    @Autowired
    AgencijaRepozitorijum agencijaRepozitorijum;

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
            @RequestParam(value = "povrsinamin", required = false) String povrsinamin,
            @RequestParam(value = "povrsinamax", required = false) String povrsinamax,
            @RequestParam(value = "cenamin", required = false) String cenamin,
            @RequestParam(value = "cenamax", required = false) String cenamax,
            @RequestParam(value = "prodaja", required = false) String prodaja,
            @RequestParam(value = "tip", required = false) String tip,
            @RequestHeader("authorization") String token) {

        if(lokacija == null || lokacija.isEmpty()) { lokacija = "%";}
        System.out.println(lokacija);

        if(povrsinamin == null || povrsinamin.isEmpty()) { povrsinamin = "0";}
        else {
            try {
                Double.parseDouble(povrsinamin);
            } catch (NumberFormatException e) {
                povrsinamin = "0";
            }
        }
        System.out.println(povrsinamin);

        if(povrsinamax == null || povrsinamax.isEmpty()) { povrsinamax = "2147483647";}
        else {
            try {
                Double.parseDouble(povrsinamax);
            } catch (NumberFormatException e) {
                povrsinamax = "2147483647";
            }
        }
        System.out.println(povrsinamax);

        if(cenamin == null || cenamin.isEmpty()) { cenamin = "0";}
        else {
            try {
                Double.parseDouble(cenamin);
            } catch (NumberFormatException e) {
                cenamin = "0";
            }
        }

        if(cenamax == null || cenamax.isEmpty()) { cenamax = "2147483647";}
        else {
            try {
                Double.parseDouble(cenamax);
            } catch (NumberFormatException e) {
                cenamax = "2147483647";
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

        System.out.println(lokacija + povrsinamin + povrsinamax + cenamin + cenamax + prodaja + tip);
        List<Nekretnina> nekretnine = nekretninaRepozitorijum.filter(lokacija, povrsinamin, povrsinamax, cenamin, cenamax, prodaja, tip);
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
            if (korisnik != null && korisnik.isActive()) {
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

            if (korisnik != null && korisnik.isActive()) {
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

                if (korisnik != null && korisnik.isActive()) {
                    Ocena ocena = likeRepozitorijum.liked(korisnik.getId(), nekretnina.getId());
                    System.out.println(korisnik.getId() + "bruhimics" +  nekretnina.getId());
                    if (ocena != null) {
                        System.out.println(ocena.likeDislike);
                        nekretninaDTO.setLiked(ocena.likeDislike);
                    }
                };

                List<Termin> termini = terminRepozitorijum.terminiNekretnine(nekretnina.getId());
                List<TerminDTO> terminDTOi = new ArrayList<>();
                for (Termin termin : termini) {
                    TerminDTO terminDTO = new TerminDTO();
                    terminDTO.setDate(termin.getDatum());
                    terminDTOi.add(terminDTO);
                }
                nekretninaDTO.setTermini(terminDTOi);

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

            if (korisnik != null && korisnik.isActive()) {
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

            if (korisnik != null && korisnik.isActive()) {
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

            if (korisnik != null && korisnik.isActive()) {
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

            if (korisnik != null && korisnik.isActive()) {
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
                        terminDTO.setAccepted(termin.isAccepted());
                        terminDTO.setVidjen(termin.isVidjen());
                        if (!termin.isVidjen()) {
                            terminRepozitorijum.vidi(termin.getId());
                        }
                        terminDTOi.add(terminDTO);
                    }

                    return ResponseEntity.ok(terminDTOi);
                }

                if (korisnik.getRole() == enumRole.KORISNIK) {
                    List<Termin> termini = new ArrayList<>();
                    termini.addAll(terminRepozitorijum.mojiTermini(korisnik.getId()));
                    termini.addAll(terminRepozitorijum.mojiZavrseniTermini(korisnik.getId()));
                    List<TerminDTO> terminDTOi = new ArrayList<>();
                    for (Termin termin : termini) {
                        TerminDTO terminDTO = new TerminDTO();
                        terminDTO.setIdTermina(termin.getId());
                        terminDTO.setDate(termin.getDatum());
                        terminDTO.setId(termin.getNekretnina().getId());
                        terminDTO.setAccepted(termin.isAccepted());
                        terminDTO.setZavrsen(termin.isZavrsen());
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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {
                    Termin termin = terminRepozitorijum.nadji(terminDTO.getIdTermina());
                    System.out.println("asdasdasdadasdasd");
                    if (termin != null) {
                        System.out.println(termin.getId());
                        Nekretnina nekretnina = nekretninaRepozitorijum.findById(termin.getNekretnina().getId());
                        System.out.println("asdasdasdadasdasd");
                        System.out.println(nekretnina.getKorisnik().getId());
                        System.out.println(korisnik.getId());

                        if (Objects.equals(nekretnina.getKorisnik().getId(), korisnik.getId())) {
                            if (terminDTO.getAccepted() != null) {
                                if (terminDTO.getAccepted()) {
                                    terminRepozitorijum.prihvati(terminDTO.getIdTermina());
                                } else {
                                    terminRepozitorijum.odbij(terminDTO.getIdTermina());
                                }
                                return new ResponseEntity<>(null, HttpStatus.OK);
                            }

                        }
                    }

                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/zavrsiobilazak")
    public ResponseEntity<TerminDTO> zavrsiObilazak(@RequestBody @Validated TerminDTO terminDTO, @RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {
                    Termin termin = terminRepozitorijum.nadji(terminDTO.getIdTermina());
                    System.out.println("asdasdasdadasdasd");
                    if (termin != null) {
                        System.out.println(termin.getId());
                        Nekretnina nekretnina = nekretninaRepozitorijum.findById(termin.getNekretnina().getId());
                        System.out.println("asdasdasdadasdasd");
                        System.out.println(nekretnina.getKorisnik().getId());
                        System.out.println(korisnik.getId());

                        if (Objects.equals(nekretnina.getKorisnik().getId(), korisnik.getId())) {
                            if (termin.isAccepted()) {
                                terminRepozitorijum.zavrsi(terminDTO.getIdTermina());
                                terminRepozitorijum.odbij(terminDTO.getIdTermina());

                                return new ResponseEntity<>(null, HttpStatus.OK);
                            }
                            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                        }
                    }

                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/novarecenzija")
    public ResponseEntity<RecenzijaDTO> novaRecenzija(@RequestBody @Validated RecenzijaDTO recenzijaDTO, @RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.KORISNIK) {
                    Termin termin = terminRepozitorijum.nadjiBiloKojiTermin(recenzijaDTO.idTermina);

                    if (termin != null && termin.isZavrsen() && Objects.equals(termin.getKorisnik().getId(), korisnik.getId())) {
                        if (recenzijaDTO.ocena <6 && recenzijaDTO.ocena > -1) {
                            Nekretnina nekretnina = nekretninaRepozitorijum.findById(termin.getNekretnina().getId());
                            recenzijaRepozitorijum.insert(korisnik.getId(), nekretnina.getKorisnik().getId(), recenzijaDTO.ocena, recenzijaDTO.opis);

                            return new ResponseEntity<>(null, HttpStatus.OK);
                        }
                    }
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/recenzije")
    public ResponseEntity<List<RecenzijaDTO>> recenzijeSvihAgenataAgencije(@RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.VLASNIK) {
                    List<Agent> agentiPodVlasnikom = agentRepozitorijum.nadjiSveAgentePodVlasnikom(korisnik.getId());
                    List<Integer> agentIds = agentiPodVlasnikom.stream()
                            .map(Agent::getAgent)
                            .collect(Collectors.toList());
                    List<Recenzija> recenzije = recenzijaRepozitorijum.recenzijeAgenataAgencije(agentIds);

                    List<RecenzijaDTO> recenzijaDTOi = new ArrayList<>();
                    for (Recenzija recenzija : recenzije) {
                        RecenzijaDTO recenzijaDTO = new RecenzijaDTO();
                        recenzijaDTO.setOcena(recenzija.ocena);
                        recenzijaDTO.setOpis(recenzija.opis);
                        recenzijaDTO.setAgent(recenzija.getAgent().getFirstName() + " " + recenzija.getAgent().getLastName());
                        recenzijaDTOi.add(recenzijaDTO);
                    }

                    return ResponseEntity.ok(recenzijaDTOi);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/najpopularnijenekretnine")
    public ResponseEntity<List<NekretninaDTO>> najpopularnijenekretnine(@RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.VLASNIK) {
                    List<Agent> agentiPodVlasnikom = agentRepozitorijum.nadjiSveAgentePodVlasnikom(korisnik.getId());

                    for (Agent agent : agentiPodVlasnikom) {
                        System.out.println(agent.getAgent());
                    }
                    List<Integer> agentIds = agentiPodVlasnikom.stream()
                            .map(Agent::getAgent)
                            .collect(Collectors.toList());
                    for (Integer ints : agentIds) {
                        System.out.println(ints);
                    }
                    List<Nekretnina> nekretnine = nekretninaRepozitorijum.nekretnineAgencije(agentIds);
                    for (Nekretnina nekretnina : nekretnine) {
                        System.out.println(nekretnina.getId());
                    }

                    Nekretnina najpopularnija = new Nekretnina();
                    najpopularnija.setBrojPregleda(-1);
                    Nekretnina drugaNajpopularnija = new Nekretnina();
                    drugaNajpopularnija.setBrojPregleda(-1);
                    int idNajpopularnije = 0;

                    for (Nekretnina nekretnina : nekretnine) {
                        Integer brojTermina = terminRepozitorijum.izbrojTermineZaNekretninu(nekretnina.getId());
                        System.out.println(brojTermina);
                        nekretnina.setBrojPregleda(nekretnina.getBrojPregleda() + brojTermina * 10);
                        System.out.println(nekretnina.getBrojPregleda());
                        if (nekretnina.getBrojPregleda() > najpopularnija.getBrojPregleda()) {
                            najpopularnija = nekretnina;
                            idNajpopularnije = nekretnina.getId();
                        }
                    }
                    for (Nekretnina nekretnina : nekretnine) {
                        if (nekretnina.getId() != idNajpopularnije) {
                            if (nekretnina.getBrojPregleda() > drugaNajpopularnija.getBrojPregleda()) {
                                drugaNajpopularnija = nekretnina;
                            }
                        }
                    }

                    List<NekretninaDTO> nekretnineDTOi = new ArrayList<>();

                    if (najpopularnija.getBrojPregleda() != -1) {
                        NekretninaDTO najpopularnijaDTO = new NekretninaDTO();
                        najpopularnijaDTO.setId(najpopularnija.getId());
                        najpopularnijaDTO.setLokacija(najpopularnija.getLokacija());
                        najpopularnijaDTO.setPovrsina(najpopularnija.getPovrsina());
                        najpopularnijaDTO.setCena(najpopularnija.getCena());
                        najpopularnijaDTO.setProdajaIzdaja(String.valueOf(najpopularnija.getProdajaIzdaja()));
                        najpopularnijaDTO.setTip(String.valueOf(najpopularnija.getTip()));
                        najpopularnijaDTO.setKorisnik(najpopularnija.getKorisnik().getFirstName() + najpopularnija.getKorisnik().getLastName());
                        najpopularnijaDTO.setBrojPregleda(najpopularnija.getBrojPregleda());

                        List<ImagePath> slikeUBase64 = slikaRepozitorijum.slikeNekretnine(najpopularnija.getId());
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

                        najpopularnijaDTO.setSlikeUBase64(enkodiraneSlike);
                        nekretnineDTOi.add(najpopularnijaDTO);
                    }

                    if (drugaNajpopularnija.getBrojPregleda() != -1) {
                        NekretninaDTO drugaNajpopularnijaDTO = new NekretninaDTO();
                        drugaNajpopularnijaDTO.setId(drugaNajpopularnija.getId());
                        drugaNajpopularnijaDTO.setLokacija(drugaNajpopularnija.getLokacija());
                        drugaNajpopularnijaDTO.setPovrsina(drugaNajpopularnija.getPovrsina());
                        drugaNajpopularnijaDTO.setCena(drugaNajpopularnija.getCena());
                        drugaNajpopularnijaDTO.setProdajaIzdaja(String.valueOf(drugaNajpopularnija.getProdajaIzdaja()));
                        drugaNajpopularnijaDTO.setTip(String.valueOf(drugaNajpopularnija.getTip()));
                        drugaNajpopularnijaDTO.setKorisnik(drugaNajpopularnija.getKorisnik().getFirstName() + drugaNajpopularnija.getKorisnik().getLastName());
                        drugaNajpopularnijaDTO.setBrojPregleda(drugaNajpopularnija.getBrojPregleda());

                        List<ImagePath> slikeUBase642 = slikaRepozitorijum.slikeNekretnine(drugaNajpopularnija.getId());
                        for (ImagePath slika : slikeUBase642) {
                            System.out.println(slika.getImagePath());
                        }
                        List<String> imenafajlova2 = slikeUBase642.stream()
                                .map(ImagePath::getImagePath)
                                .collect(Collectors.toList());
                        for (String slika : imenafajlova2) {
                            System.out.println(slika);
                        }

                        List<String> potpuneputanje2 = new ArrayList<>();
                        for (String slika : imenafajlova2) {
                            potpuneputanje2.add(putanja + slika);
                        }

                        for (String slika : potpuneputanje2) {
                            System.out.println(slika);
                        }

                        List<String> enkodiraneSlike2 = new ArrayList<>();
                        for (String slika : potpuneputanje2) {
                            File putanja = new File(slika);
                            try {
                                byte[] imageData = Files.readAllBytes(putanja.toPath());
                                String base64Slika = Base64.getEncoder().encodeToString(imageData);
                                enkodiraneSlike2.add(base64Slika);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        drugaNajpopularnijaDTO.setSlikeUBase64(enkodiraneSlike2);
                        nekretnineDTOi.add(drugaNajpopularnijaDTO);
                    }

                    return ResponseEntity.ok(nekretnineDTOi);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/najpopularnijiagenti")
    public ResponseEntity<List<KorisnikDTO>> najpopularnijiagenti(@RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.VLASNIK) {
                    List<Agent> agentiPodVlasnikom = agentRepozitorijum.nadjiSveAgentePodVlasnikom(korisnik.getId());

                    List<Korisnik> agenti = new ArrayList<>();
                    for (Agent agent : agentiPodVlasnikom) {
                        Korisnik hinaichigo = korisnikRepozitorijum.nadjiKorisnika(agent.getAgent());
                        agenti.add(hinaichigo);
                    }

                    int brojPoena = -1;
                    Korisnik agentpravi = new Korisnik();
                    for (Korisnik agent : agenti) {
                        List<Recenzija> recenzijeAgenta = recenzijaRepozitorijum.recenzijeAgenta(agent.getId());
                        int brojPozitivnihOcena = 0;
                        for (Recenzija recenzija : recenzijeAgenta) {
                            if (recenzija.ocena > 3) {
                                brojPozitivnihOcena = brojPozitivnihOcena + 1;
                            }
                        }
                        System.out.println(brojPozitivnihOcena);
                        brojPozitivnihOcena = brojPozitivnihOcena*15;
                        System.out.println(brojPozitivnihOcena);

                        List<Nekretnina> nekretnineAgenta = nekretninaRepozitorijum.nekretnineAgenta(agent.getId());
                        int brojPregleda = 0;
                        for (Nekretnina nekretnina : nekretnineAgenta) {
                            brojPregleda += nekretnina.getBrojPregleda();
                        }
                        System.out.println(brojPregleda);

                        int brojTermina = 0;
                        for (Nekretnina nekretnina : nekretnineAgenta) {
                            brojTermina = brojTermina + terminRepozitorijum.izbrojTermineZaPopularnost(nekretnina.getId());
                        }
                        System.out.println(brojTermina);
                        brojTermina = brojTermina*10;
                        System.out.println(brojTermina);

                        int suiseiseki = brojPozitivnihOcena + brojPregleda + brojTermina;

                        if (suiseiseki > brojPoena) {
                            brojPoena = suiseiseki;
                            agentpravi = agent;
                        }
                    }

                    KorisnikDTO agentDTO = new KorisnikDTO();
                    agentDTO.setEmail(agentpravi.getEmail());
                    agentDTO.setFirstName(agentpravi.getFirstName());
                    agentDTO.setLastName(agentpravi.getLastName());
                    agentDTO.setId(brojPoena);

                    int brojPoena2 = -1;
                    Korisnik agentpravi2 = new Korisnik();
                    for (Korisnik agent : agenti) {
                        if (!Objects.equals(agent.getId(), agentpravi.getId())) {
                            List<Recenzija> recenzijeAgenta = recenzijaRepozitorijum.recenzijeAgenta(agent.getId());
                            int brojPozitivnihOcena = 0;
                            for (Recenzija recenzija : recenzijeAgenta) {
                                if (recenzija.ocena > 3) {
                                    brojPozitivnihOcena = brojPozitivnihOcena + 1;
                                }
                            }
                            System.out.println(brojPozitivnihOcena);
                            brojPozitivnihOcena = brojPozitivnihOcena * 15;
                            System.out.println(brojPozitivnihOcena);

                            List<Nekretnina> nekretnineAgenta = nekretninaRepozitorijum.nekretnineAgenta(agent.getId());
                            int brojPregleda = 0;
                            for (Nekretnina nekretnina : nekretnineAgenta) {
                                brojPregleda += nekretnina.getBrojPregleda();
                            }
                            System.out.println(brojPregleda);

                            int brojTermina = 0;
                            for (Nekretnina nekretnina : nekretnineAgenta) {
                                brojTermina = brojTermina + terminRepozitorijum.izbrojTermineZaPopularnost(nekretnina.getId());
                            }
                            System.out.println(brojTermina);
                            brojTermina = brojTermina * 10;
                            System.out.println(brojTermina);

                            int suiseiseki = brojPozitivnihOcena + brojPregleda + brojTermina;

                            if (suiseiseki > brojPoena2) {
                                brojPoena2 = suiseiseki;
                                agentpravi2 = agent;
                            }
                        }
                    }

                    KorisnikDTO agentDTO2 = new KorisnikDTO();
                    agentDTO2.setEmail(agentpravi2.getEmail());
                    agentDTO2.setFirstName(agentpravi2.getFirstName());
                    agentDTO2.setLastName(agentpravi2.getLastName());
                    agentDTO2.setId(brojPoena2);

                    List<KorisnikDTO> korisnikDTOi = new ArrayList<>();
                    korisnikDTOi.add(agentDTO);
                    if (brojPoena2 != -1) {
                        korisnikDTOi.add(agentDTO2);
                    }


                    return ResponseEntity.ok(korisnikDTOi);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/zakljuci")
    public ResponseEntity<TerminDTO> zakljuci(@RequestBody @Validated TerminDTO terminDTO, @RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {
                    Termin termin = terminRepozitorijum.nadjiBiloKojiTermin(terminDTO.getIdTermina());
                    Nekretnina nekretnina = nekretninaRepozitorijum.findById(terminDTO.getId());
                    System.out.println("!!!!!!!!!!!!!!!!");
                    if (Objects.equals(nekretnina.getKorisnik().getId(), korisnik.getId())) {
                        if (termin != null && termin.isZavrsen() && Objects.equals(termin.getNekretnina().getId(), nekretnina.getId())) {
                            Date datum = new Date();
                            transakcijaRepozitorijum.insert(termin.getNekretnina().getId(), datum);
                            nekretninaRepozitorijum.obrisi(termin.getNekretnina().getId());
                            terminRepozitorijum.obrisiSveTermine(termin.getNekretnina().getId());
                            return new ResponseEntity<>(null, HttpStatus.OK);
                        }
                    }
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/mojizavrsenitermini")
    public ResponseEntity<List<TerminDTO>> getMojiZavrseniTermini(@RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {
                    List<Nekretnina> nekretnine = nekretninaRepozitorijum.sveNekretnineAgenta(korisnik.getId());

                    List<Integer> ids = nekretnine.stream()
                            .map(Nekretnina::getId)
                            .collect(Collectors.toList());
                    for (Integer id : ids) {
                        System.out.println(id);
                    }
                    List<Termin> termini = terminRepozitorijum.zavrseniTerminiAgenta(ids);
                    Iterator<Termin> iterator = termini.iterator();
                    while (iterator.hasNext()) {
                        Termin termin = iterator.next();
                        Transakcija transakcija = transakcijaRepozitorijum.nadji(termin.getNekretnina().getId());
                        System.out.println(termin.getDatum());
                        if (transakcija != null) {
                            iterator.remove(); // Remove the current element using the iterator
                        }
                    }
                    List<TerminDTO> terminDTOi = new ArrayList<>();
                    for (Termin termin : termini) {
                        TerminDTO terminDTO = new TerminDTO();
                        terminDTO.setIdTermina(termin.getId());
                        terminDTO.setDate(termin.getDatum());
                        terminDTO.setId(termin.getNekretnina().getId());
                        terminDTO.setAccepted(termin.isAccepted());
                        terminDTOi.add(terminDTO);
                    }

                    return ResponseEntity.ok(terminDTOi);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/izvestaj/{datum}")
    public ResponseEntity<IzvestajDTO> getIzvestaj(@PathVariable String datum, @RequestHeader("authorization") String token) {

        String[] godinaIMesec = datum.split("-");

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.ADMIN) {
                    System.out.println(Integer.parseInt(godinaIMesec[0]) + "smrdim" +  Integer.parseInt(godinaIMesec[1]));
                    List<Transakcija> transakcije = transakcijaRepozitorijum.transakcijeZaMesec(Integer.parseInt(godinaIMesec[0]), Integer.parseInt(godinaIMesec[1]));
                    double promet = 0;
                    double zarada = 0;

                    for (Transakcija transakcija : transakcije) {
                        if (transakcija.getNekretnina().getProdajaIzdaja() == enumProdajaIzdaja.IZDAJA) {
                            promet = promet + transakcija.getNekretnina().getCena();
                            zarada = zarada + transakcija.getNekretnina().getCena() * 0.01;
                        }

                        if (transakcija.getNekretnina().getProdajaIzdaja() == enumProdajaIzdaja.PRODAJA) {
                            promet = promet + transakcija.getNekretnina().getCena() * transakcija.getNekretnina().getPovrsina();
                            zarada = zarada + (transakcija.getNekretnina().getCena() * transakcija.getNekretnina().getPovrsina()) * 0.001;
                        }
                    }
                    IzvestajDTO izvestajDTO = new IzvestajDTO();
                    izvestajDTO.setPromet(promet);
                    izvestajDTO.setZarada(zarada);

                    return ResponseEntity.ok(izvestajDTO);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/izmeniagenciju")
    public ResponseEntity<AgencijaDTO> izmeniAgenciju(@RequestBody @Validated AgencijaDTO agencijaDTO, @RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.VLASNIK) {
                    Agencija agencija = agencijaRepozitorijum.nadji(korisnik.getId());

                    Agencija agencijaSaIstimImenom = agencijaRepozitorijum.findFirstByIme(agencijaDTO.getIme());
                    if(agencijaSaIstimImenom == null) {
                        if (agencijaDTO.getOpis().isEmpty()) {
                            agencijaRepozitorijum.izmeni(agencijaDTO.getIme(), agencija.getOpis(), agencija.getId());
                        }
                        else if (agencijaDTO.getIme().isEmpty()) {
                            agencijaRepozitorijum.izmeni(agencija.getIme(), agencijaDTO.getOpis(), agencija.getId());
                        }
                        else {
                            agencijaRepozitorijum.izmeni(agencijaDTO.getIme(), agencijaDTO.getOpis(), agencija.getId());
                        }

                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }

                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/izmeninekretninu")
    public ResponseEntity<NekretninaDTO> izmeniNekretninu(@RequestBody @Validated NekretninaDTO nekretninaDTO, @RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.AGENT || korisnik.getRole() == enumRole.VLASNIK) {
                    Nekretnina nekretnina = nekretninaRepozitorijum.findById(nekretninaDTO.getId());

                    if (nekretnina != null && Objects.equals(nekretnina.getKorisnik().getId(), korisnik.getId())) {

                        try {
                            enumProdajaIzdaja.valueOf(nekretninaDTO.getProdajaIzdaja());
                        }
                        catch (Exception e) {
                            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                        }

                        if (nekretninaDTO.getCena() == 0) {
                            nekretninaRepozitorijum.izmeni(nekretnina.getCena(), nekretninaDTO.getProdajaIzdaja(), nekretnina.getId());
                        }
                        else if (nekretninaDTO.getProdajaIzdaja().isEmpty()) {
                            nekretninaRepozitorijum.izmeni(nekretninaDTO.getCena(), String.valueOf(nekretnina.getProdajaIzdaja()), nekretnina.getId());
                        }
                        else {
                            nekretninaRepozitorijum.izmeni(nekretninaDTO.getCena(), nekretninaDTO.getProdajaIzdaja(), nekretnina.getId());
                        }

                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }

                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/obrisinekretninu")
    public ResponseEntity<NekretninaDTO> obrisiNekretninu(@RequestBody @Validated NekretninaDTO nekretninaDTO, @RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.ADMIN) {
                    Nekretnina nekretnina = nekretninaRepozitorijum.findById(nekretninaDTO.getId());
                    if (nekretnina != null) {
                        nekretninaRepozitorijum.obrisi(nekretnina.getId());
                        terminRepozitorijum.obrisiSveTermine(nekretnina.getId());

                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                }
                if (korisnik.getRole() == enumRole.VLASNIK) {
                    List<Agent> agentiPodVlasnikom = agentRepozitorijum.nadjiSveAgentePodVlasnikom(korisnik.getId());
                    List<Integer> agentIds = agentiPodVlasnikom.stream()
                            .map(Agent::getAgent)
                            .collect(Collectors.toList());
                    for (Integer ints : agentIds) {
                        System.out.println(ints);
                    }
                    for (Integer id : agentIds) {
                        Nekretnina nekretnina = nekretninaRepozitorijum.odrediVlasnistvo(nekretninaDTO.getId(), id);

                        if (nekretnina != null) {
                            nekretninaRepozitorijum.obrisi(nekretnina.getId());
                            terminRepozitorijum.obrisiSveTermine(nekretnina.getId());

                            return new ResponseEntity<>(null, HttpStatus.OK);
                        }
                    }
                }
                if (korisnik.getRole() == enumRole.AGENT) {
                    Nekretnina nekretnina = nekretninaRepozitorijum.findById(nekretninaDTO.getId());
                    if (Objects.equals(nekretnina.getKorisnik().getId(), korisnik.getId())) {
                        nekretninaRepozitorijum.obrisi(nekretnina.getId());
                        terminRepozitorijum.obrisiSveTermine(nekretnina.getId());

                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }

                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/obrisikorisnika")
    public ResponseEntity<NekretninaDTO> obrisiNekretninu(@RequestBody @Validated KorisnikDTO korisnikDTO, @RequestHeader("authorization") String token) {

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

            if (korisnik != null && korisnik.isActive()) {
                if (korisnik.getRole() == enumRole.ADMIN) {
                    Korisnik korisnikZaBrisanje = korisnikRepozitorijum.findByEmail(korisnikDTO.getEmail());

                    if (korisnikZaBrisanje != null) {
                        korisnikRepozitorijum.obrisi(korisnikZaBrisanje.getId());
                        agentRepozitorijum.obrisiOdnos(korisnikZaBrisanje.getId());
                        List<Nekretnina> nekretnineKorisnika = nekretninaRepozitorijum.nekretnineAgenta(korisnikZaBrisanje.getId());
                        for (Nekretnina nekretnina : nekretnineKorisnika) {
                            nekretninaRepozitorijum.obrisi(nekretnina.getId());
                            terminRepozitorijum.obrisiSveTermine(nekretnina.getId());
                        }

                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                }

                if (korisnik.getRole() == enumRole.VLASNIK) {
                    Korisnik korisnikZaBrisanje = korisnikRepozitorijum.findByEmail(korisnikDTO.getEmail());

                    if (korisnikZaBrisanje != null) {
                        Agent agent = agentRepozitorijum.nadjiAgenta(korisnikZaBrisanje.getId(), korisnik.getId());
                        if (agent != null) {
                            agentRepozitorijum.obrisiOdnos(korisnikZaBrisanje.getId());
                            korisnikRepozitorijum.obrisi(korisnikZaBrisanje.getId());
                            List<Nekretnina> nekretnineKorisnika = nekretninaRepozitorijum.nekretnineAgenta(korisnikZaBrisanje.getId());
                            for (Nekretnina nekretnina : nekretnineKorisnika) {
                                nekretninaRepozitorijum.obrisi(nekretnina.getId());
                                terminRepozitorijum.obrisiSveTermine(nekretnina.getId());
                            }

                            return new ResponseEntity<>(null, HttpStatus.OK);
                        }

                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
}
