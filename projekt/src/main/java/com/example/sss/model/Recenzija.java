package com.example.sss.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recenzije")
public class Recenzija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "korisnik_id")
    private Korisnik korisnik;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Korisnik agent;

    @Column(nullable = false)
    public int ocena;

    @Column(nullable = false)
    public String opis;
}
