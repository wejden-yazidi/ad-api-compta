package com.adcaisse.compta.dto;

import java.util.Date;

public class ClientPartnerDto {
     
	private String idClient;
	private String idPartenaire;
	private String nom;
	private String prenom;
	private String qrCode;
	private String idJournee;
	private String sourceCreation;
	private String tokenNotification;
	private String otp;
	private String idGroupeClient;
	private String raisonsocial;
	private String type; // PP / PM
	private String matfiscale;
	private String adress;
	private String urlImage;
	private String mail;
	private Date dateNaissance;
	
	
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
	private String password;
	
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
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getIdJournee() {
		return idJournee;
	}
	public void setIdJournee(String idJournee) {
		this.idJournee = idJournee;
	}
	public String getSourceCreation() {
		return sourceCreation;
	}
	public void setSourceCreation(String sourceCreation) {
		this.sourceCreation = sourceCreation;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getIdGroupeClient() {
		return idGroupeClient;
	}
	public void setIdGroupeClient(String idGroupeClient) {
		this.idGroupeClient = idGroupeClient;
	}
	public String getRaisonsocial() {
		return raisonsocial;
	}
	public void setRaisonsocial(String raisonsocial) {
		this.raisonsocial = raisonsocial;
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
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getUrlImage() {
		return urlImage;
	}
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Date getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
