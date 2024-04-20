package com.example.sss.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecenzijaDTO {
    public int idTermina;
    public int ocena;
    public String opis;
    public String agent;
}
