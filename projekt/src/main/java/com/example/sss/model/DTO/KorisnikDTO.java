package com.example.sss.model.DTO;

import com.example.sss.model.Korisnik;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Setter
//@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KorisnikDTO {
    public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public String getAdresa() {
		return adresa;
	}

	public String getUloga() {
		return uloga;
	}

	public Integer id;
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String brojTelefona;
    public String adresa;
    public String uloga;

    public KorisnikDTO(Korisnik kreirani) {
        this.email = kreirani.getEmail();
        this.password = kreirani.getPassword();
        this.firstName = kreirani.getFirstName();
        this.lastName = kreirani.getLastName();
        this.brojTelefona = kreirani.getNumTel();
        this.adresa = kreirani.getAddress();
        this.uloga = String.valueOf(kreirani.getRole());
    }
}
