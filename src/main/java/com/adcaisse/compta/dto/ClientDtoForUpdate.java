package com.adcaisse.compta.dto;

import java.util.Date;

public class ClientDtoForUpdate {
     
	private String idClient;
	private String nom;
	private String prenom;
	private Date dateNaissance; 
	private String email;
	private String genre;
	
	 private String matfiscale;
	 private String raisonsocial;
	 private String numpatente;
	 private String siegesocial;
	 private String adress;
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public Date getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
		
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public String getMatfiscale() {
		return matfiscale;
	}
	public void setMatfiscale(String matfiscale) {
		this.matfiscale = matfiscale;
	}
	public String getRaisonsocial() {
		return raisonsocial;
	}
	public void setRaisonsocial(String raisonsocial) {
		this.raisonsocial = raisonsocial;
	}
	public String getNumpatente() {
		return numpatente;
	}
	public void setNumpatente(String numpatente) {
		this.numpatente = numpatente;
	}
	public String getSiegesocial() {
		return siegesocial;
	}
	public void setSiegesocial(String siegesocial) {
		this.siegesocial = siegesocial;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
	
	
	
	
	
	
		
}
