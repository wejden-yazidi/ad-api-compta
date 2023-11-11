package com.adcaisse.compta.dto;

import java.util.Date;

public class CadeauDto {
     	
	private Date date;
	private String idparteanire;
	private String  type;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getIdparteanire() {
		return idparteanire;
	}
	public void setIdparteanire(String idparteanire) {
		this.idparteanire = idparteanire;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
