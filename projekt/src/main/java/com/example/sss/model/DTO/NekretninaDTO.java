package com.example.sss.model.DTO;

import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;
import com.example.sss.model.enumProdajaIzdavanje;
import com.example.sss.model.enumTip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Getter
//@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NekretninaDTO {
    

	public Integer id;
    public String lokacija;
    public double povrsina;
    public double cena;
    public enumProdajaIzdavanje prodajaIzdavanje;
    public enumTip tip;
    public Korisnik korisnik;
    
    public NekretninaDTO(Nekretnina nekretnina) {
    	this.lokacija = nekretnina.getLokacija();
    	this.povrsina = nekretnina.getPovrsina();
    	this.cena = nekretnina.getCena();
    	this.prodajaIzdavanje = nekretnina.getProdajaIzdavanje();
    	this.tip = nekretnina.getTip();
    	this.korisnik = nekretnina.getKorisnik();
    }
    
    public NekretninaDTO() {
		// TODO Auto-generated constructor stub
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
		this.tip = tip;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

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
		return tip;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}
}
