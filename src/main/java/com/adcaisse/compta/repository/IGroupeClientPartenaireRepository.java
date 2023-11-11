package com.adcaisse.compta.repository;

import com.bprice.persistance.model.GroupeClientPartenaire;
import com.bprice.persistance.model.MvtSolde;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IGroupeClientPartenaireRepository extends MongoRepository<GroupeClientPartenaire,String> {
    public GroupeClientPartenaire findByIdGroupeClientPartenaire(String id);
    public List<GroupeClientPartenaire> findAllByIdPartenaireOrderByDateCreationDesc(String id);
    public List<GroupeClientPartenaire> findAllByIdPartenaireAndIsActif(String id,Short type);

}
