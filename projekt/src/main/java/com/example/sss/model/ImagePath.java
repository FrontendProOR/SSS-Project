package com.example.sss.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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