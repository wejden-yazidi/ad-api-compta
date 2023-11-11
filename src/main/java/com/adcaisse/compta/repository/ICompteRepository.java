package com.adcaisse.compta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bprice.persistance.model.Compte;

@Repository

public interface ICompteRepository extends MongoRepository<Compte, String> {

}
