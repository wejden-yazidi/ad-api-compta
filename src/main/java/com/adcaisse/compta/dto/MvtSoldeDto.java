package com.adcaisse.compta.dto;


public class MvtSoldeDto {
     
    private String idClientPartenaire;
    private Float montant;
    private String sens;
    private String Source;
    private String typeOperation;
    
	public String getIdClientPartenaire() {
		return idClientPartenaire;
	}
	public void setIdClientPartenaire(String idClientPartenaire) {
		this.idClientPartenaire = idClientPartenaire;
	}
	public Float getMontant() {
		return montant;
	}
	public void setMontant(Float montant) {
		this.montant = montant;
	}
	public String getSens() {
		return sens;
	}
	public void setSens(String sens) {
		this.sens = sens;
	}
	public String getSource() {
		return Source;
	}
	public void setSource(String source) {
		Source = source;
	}
	public String getTypeOperation() {
		return typeOperation;
	}
	public void setTypeOperation(String typeOperation) {
		this.typeOperation = typeOperation;
	}
	
	
    
    
}
