package com.example.sss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
//@Getter
//@Setter
@Data
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
    
    public Integer getId() {
		return id;
	}

	public String getLokacija() {
		return lokacija;
	}

	public double getPovrsina() {
		return povrsina;
	}

	public double getCena() {
		return cena;
	}

	public enumProdajaIzdavanje getProdajaIzdavanje() {
		return prodajaIzdavanje;
	}

	public enumTip getTip() {
		return Tip;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setLokacija(String lokacija) {
		this.lokacija = lokacija;
	}

	public void setPovrsina(double povrsina) {
		this.povrsina = povrsina;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public void setProdajaIzdavanje(enumProdajaIzdavanje prodajaIzdavanje) {
		this.prodajaIzdavanje = prodajaIzdavanje;
	}

	public void setTip(enumTip tip) {
		Tip = tip;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
}
