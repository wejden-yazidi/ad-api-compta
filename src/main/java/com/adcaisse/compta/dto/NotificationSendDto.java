package com.adcaisse.compta.dto;

import java.util.List;

public class NotificationSendDto {
     
	String idClientPartner; 
	String idNotification;
	String idPartenaire;
	String type;
	short factif;
	List<String> listIdClientPartner;
	String sendToAll;
	short isSlider;
	
	public String getIdClientPartner() {
		return idClientPartner;
	}
	public void setIdClientPartner(String idClientPartner) {
		this.idClientPartner = idClientPartner;
	}
	public String getIdNotification() {
		return idNotification;
	}
	public void setIdNotification(String idNotification) {
		this.idNotification = idNotification;
	}
	public String getIdPartenaire() {
		return idPartenaire;
	}
	public void setIdPartenaire(String idPartenaire) {
		this.idPartenaire = idPartenaire;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public short getFactif() {
		return factif;
	}
	public void setFactif(short factif) {
		this.factif = factif;
	}
	public List<String> getListIdClientPartner() {
		return listIdClientPartner;
	}
	public void setListIdClientPartner(List<String> listIdClientPartner) {
		this.listIdClientPartner = listIdClientPartner;
	}
	
	public short getIsSlider() {
		return isSlider;
	}
	public void setIsSlider(short isSlider) {
		this.isSlider = isSlider;
	}
	public String getSendToAll() {
		return sendToAll;
	}
	public void setSendToAll(String sendToAll) {
		this.sendToAll = sendToAll;
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
		
}
