package com.example.sss.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nekretnine")
public class Nekretnina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String lokacija;

    @Column(nullable = false)
    private double povrsina;

    @Column(nullable = false)
    private double cena;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private enumProdajaIzdavanje prodajaIzdavanje;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private enumTip Tip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;
}
