package com.example.sss.model;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;

@Entity
//@Getter
//@Setter
@Data
@Table(name = "korisnici")
public class Korisnik {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String numTel;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private enumRole role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "korisnik")
    private List<Nekretnina> nekretnine;
    
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

	public String getNumTel() {
		return numTel;
	}

	public String getAddress() {
		return address;
	}

	public String getToken() {
		return token;
	}

	public enumRole getRole() {
		return role;
	}

	public List<Nekretnina> getNekretnine() {
		return nekretnine;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setRole(enumRole role) {
		this.role = role;
	}

	public void setNekretnine(List<Nekretnina> nekretnine) {
		this.nekretnine = nekretnine;
	}
}
