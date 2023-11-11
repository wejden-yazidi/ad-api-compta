package com.adcaisse.compta.dto;

public class InputClientPartnerDto {
     
	private String idClient;
	private String idPartner;
	private String rechercheValue;
	private String qrCode;
	private String operation;
	private String nTel;
	private Double amount;
	private String idJournee;
	private String typeOperation;
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public String getIdPartner() {
		return idPartner;
	}
	public void setIdPartner(String idPartner) {
		this.idPartner = idPartner;
	}
	public String getRechercheValue() {
		return rechercheValue;
	}
	public void setRechercheValue(String rechercheValue) {
		this.rechercheValue = rechercheValue;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getnTel() {
		return nTel;
	}
	public void setnTel(String nTel) {
		this.nTel = nTel;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getIdJournee() {
		return idJournee;
	}
	public void setIdJournee(String idJournee) {
		this.idJournee = idJournee;
	}
	public String getTypeOperation() {
		return typeOperation;
	}
	public void setTypeOperation(String typeOperation) {
		this.typeOperation = typeOperation;
	}
	
	
	
	
	
}
