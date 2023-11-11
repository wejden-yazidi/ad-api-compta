package com.adcaisse.compta.dto;

public class TransfertDto {
     	
	private String idClientPartnerSource;
	private String idClientPartnerCible;
	private String idPartenaire;
	private Double amount;
	private String typeOperation;
	public String getIdClientPartnerSource() {
		return idClientPartnerSource;
	}
	public void setIdClientPartnerSource(String idClientPartnerSource) {
		this.idClientPartnerSource = idClientPartnerSource;
	}
	public String getIdClientPartnerCible() {
		return idClientPartnerCible;
	}
	public void setIdClientPartnerCible(String idClientPartnerCible) {
		this.idClientPartnerCible = idClientPartnerCible;
	}
	public String getIdPartenaire() {
		return idPartenaire;
	}
	public void setIdPartenaire(String idPartenaire) {
		this.idPartenaire = idPartenaire;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTypeOperation() {
		return typeOperation;
	}
	public void setTypeOperation(String typeOperation) {
		this.typeOperation = typeOperation;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
	
	
	
}
