package com.example.sss.model.DTO;

import com.example.sss.model.Agencija;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VlasnikAgencija {
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String brojTelefona;
    public String adresa;
    public String uloga;
    private String ime;
    private String opis;
}
