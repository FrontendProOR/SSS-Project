package com.example.sss.model;

import jakarta.persistence.*;

@Entity
@Table(name = "slike")
public class ImagePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "nekretnina_id", referencedColumnName = "id")
    private Nekretnina nekretnina;

}