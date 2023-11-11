package com.adcaisse.compta.dto;

public class ClientPartnerAfterConnexionDto {
     
	private String idClientPartner;
	private short isconnected;
	private String tokenNotification;
	private String password;
	
	public String getIdClientPartner() {
		return idClientPartner;
	}
	public void setIdClientPartner(String idClientPartner) {
		this.idClientPartner = idClientPartner;
	}
	public short getIsconnected() {
		return isconnected;
	}
	public void setIsconnected(short isconnected) {
		this.isconnected = isconnected;
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

	
	
	
	
	
	
	
	
}
