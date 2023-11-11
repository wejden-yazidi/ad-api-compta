package com.adcaisse.compta.dto;

import java.util.Date;

import com.bprice.persistance.model.Image;
import com.fasterxml.jackson.annotation.JsonFormat;

public class NotificationDto {
     
	private String idPartenaire;
	private String typeNotification;
	private String titre;
	private String body;
	private String description;	
	private Date dateDebut;
	private Date dateFin;
	private Image imagesNotif;
	private short isSlider;
	public String getIdPartenaire() {
		return idPartenaire;
	}
	public void setIdPartenaire(String idPartenaire) {
		this.idPartenaire = idPartenaire;
	}
	public String getTypeNotification() {
		return typeNotification;
	}
	public void setTypeNotification(String typeNotification) {
		this.typeNotification = typeNotification;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateDebut() {
		return dateDebut;
	}
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}
	public Date getDateFin() {
		return dateFin;
	}
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}
	
	public short getIsSlider() {
		return isSlider;
	}
	public void setIsSlider(short isSlider) {
		this.isSlider = isSlider;
	}
	public Image getImagesNotif() {
		return imagesNotif;
	}
	public void setImagesNotif(Image imagesNotif) {
		this.imagesNotif = imagesNotif;
	}
	
	
	
	
	
	
		
}
