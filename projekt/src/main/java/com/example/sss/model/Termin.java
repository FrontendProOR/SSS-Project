package com.example.sss.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "termini")
public class Termin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;

    @Column(name = "datum")
    private Date datum;

    @ManyToOne
    @JoinColumn(name = "nekretnina_id")
    private Nekretnina nekretnina;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean accepted;

    @Column(nullable = false)
    private boolean zavrsen;

    @Column(nullable = false)
    private boolean vidjen;

}