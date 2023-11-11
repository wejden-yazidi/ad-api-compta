package com.adcaisse.compta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bprice.persistance.model.Compte;

@Service
public interface ICompteService {
	
	public Compte ajouterCompte(Compte c);
	public void supprimerCompte(String idCompte);
	public Compte retirveOneComote(String id);
	public List<Compte> retrieveAll();
	 
}
