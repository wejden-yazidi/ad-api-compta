package com.adcaisse.compta.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bprice.persistance.model.MvtSolde;

/**
 * Created by Hassine on 15/02/2020.
 */
@Repository
public interface IMvtSoldeRepository extends MongoRepository<MvtSolde,String> {
    public MvtSolde findByIdMvtSolde(String idMvtSolde);
    public List<MvtSolde> findAllByIdClientPartenaireOrderByDateOperationDesc(String idClientPartenaire);    
}
