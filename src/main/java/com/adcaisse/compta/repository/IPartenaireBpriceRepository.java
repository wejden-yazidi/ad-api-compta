package com.adcaisse.compta.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bprice.persistance.model.PartenaireBprice;

@Repository
public interface IPartenaireBpriceRepository extends MongoRepository<PartenaireBprice,String> {
	
    public PartenaireBprice findByIdPartenaire(String idPartenaire);

}
