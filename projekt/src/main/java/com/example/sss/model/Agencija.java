package com.example.sss.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "agencije")
public class Agencija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String ime;

    @Column(nullable = false)
    private String opis;

    @Column(nullable = false)
    private boolean active;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;
}
