package com.example.sss.model.DTO;

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
    public String prodajaIzdavanje;
    public String tip;
    public String korisnik;
}
