package com.example.sss.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NekretninaDTO {

    public Integer id;
    public String lokacija;
    public double povrsina;
    public double cena;
    public String prodajaIzdaja;
    public String tip;
    public String korisnik;
    public Integer brojPregleda;
    public List<String> slikeUBase64;
    public Boolean liked;
    public List<TerminDTO> termini;
}
