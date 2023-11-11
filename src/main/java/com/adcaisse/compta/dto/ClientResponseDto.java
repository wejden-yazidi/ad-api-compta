package com.adcaisse.compta.dto;

import java.util.Date;



public class ClientResponseDto {
     
	private String nom;
	private String prenom;
	private Date dateNaissance; 
	private String nTel;
	private String email;
	private String genre;
    private Date dateCreation;
    private String qrCodePartn;
    private String idClient;
    private String idClientPartenaire;
	private String idPartenaire;
	private Double soldePartn;
	private String tokenNotification;
	private String password;
	private short isconnected; 
	private short isActive;
	private String idGroupeClient;
	private String type; //PP : personne physique / PM P.morale
	private String matfiscale;
	private String raisonsocial;
	private String numpatente;
	private String siegesocial;
	private String adress;
	private String image;
	
	
    
	
	
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public String getIdPartenaire() {
		return idPartenaire;
	}
	public void setIdPartenaire(String idPartenaire) {
		this.idPartenaire = idPartenaire;
	}
	public Double getSoldePartn() {
		return soldePartn;
	}
	public void setSoldePartn(Double soldePartn) {
		this.soldePartn = soldePartn;
	}
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
	public String getnTel() {
		return nTel;
	}
	public void setnTel(String nTel) {
		this.nTel = nTel;
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
	
	public Date getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	public String getTokenNotification() {
		return tokenNotification;
	}
	public void setTokenNotification(String tokenNotification) {
		this.tokenNotification = tokenNotification;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public short getIsconnected() {
		return isconnected;
	}
	public void setIsconnected(short isconnected) {
		this.isconnected = isconnected;
	}
	public String getQrCodePartn() {
		return qrCodePartn;
	}
	public void setQrCodePartn(String qrCodePartn) {
		this.qrCodePartn = qrCodePartn;
	}
	public String getIdClientPartenaire() {
		return idClientPartenaire;
	}
	public void setIdClientPartenaire(String idClientPartenaire) {
		this.idClientPartenaire = idClientPartenaire;
	}
	public short getIsActive() {
		return isActive;
	}
	public void setIsActive(short isActive) {
		this.isActive = isActive;
	}
	public String getIdGroupeClient() {
		return idGroupeClient;
	}
	public void setIdGroupeClient(String idGroupeClient) {
		this.idGroupeClient = idGroupeClient;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public void setAdress(String adresse) {
		this.adress = adresse;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
	
	
	
	
	
	
	
	
	
	
		
}
