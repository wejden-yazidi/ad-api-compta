package com.adcaisse.compta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bprice.persistance.model.Journall;

public interface JournalRepository extends MongoRepository<Journall, String>{

}
