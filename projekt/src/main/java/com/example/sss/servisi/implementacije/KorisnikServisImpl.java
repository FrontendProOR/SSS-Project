package com.example.sss.servisi.implementacije;

import com.example.sss.model.DTO.KorisnikDTO;
import com.example.sss.model.Korisnik;
import com.example.sss.model.enumRole;
import com.example.sss.repozitorijumi.KorisnikRepozitorijum;
import com.example.sss.servisi.KorisnikServis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class KorisnikServisImpl implements KorisnikServis {

    @Autowired
    private KorisnikRepozitorijum korisnikRepozitorijum;

    @Override
    public List<Korisnik> getAll() {
        System.out.println(korisnikRepozitorijum.findAll());
        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFF");
        return korisnikRepozitorijum.findAll();
    }

    @Override
    public Korisnik createUser(KorisnikDTO korisnikDTO) {

        Optional<Korisnik> korisnik = korisnikRepozitorijum.findFirstByEmail(korisnikDTO.getEmail());

        if(korisnik.isPresent()){
            return null;
        }
        System.out.println("GUGUGGUGUGUGU");
        Korisnik novi = new Korisnik();
        novi.setEmail(korisnikDTO.getEmail());
        novi.setPassword(korisnikDTO.getPassword());
        novi.setFirstName(korisnikDTO.getFirstName());
        novi.setLastName(korisnikDTO.getLastName());
        novi.setNumTel(korisnikDTO.getBrojTelefona());
        novi.setAddress(korisnikDTO.getAdresa());
        System.out.println("ASHDASDASDHKJSADHA");
        novi.setRole(enumRole.valueOf(korisnikDTO.getUloga().toUpperCase()));

        String jwtToken = generateJwtToken(novi);
        novi.setToken(jwtToken);

        novi = korisnikRepozitorijum.save(novi);

        return novi;
    }

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    // JWT token metod
    private String generateJwtToken(Korisnik korisnik) {
        long expirationTime = 180000;

        return Jwts.builder()
                .setSubject(korisnik.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }
}
