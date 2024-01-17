package com.example.sss.model.DTO;

import com.example.sss.model.Korisnik;
import com.example.sss.model.Nekretnina;
import com.example.sss.model.enumProdajaIzdavanje;
import com.example.sss.model.enumTip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}
