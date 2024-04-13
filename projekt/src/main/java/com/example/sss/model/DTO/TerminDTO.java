package com.example.sss.model.DTO;

import com.example.sss.model.Nekretnina;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TerminDTO {
    private Date date;
    private Integer id;
}
