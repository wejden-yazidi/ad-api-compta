package com.adcaisse.compta.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bprice.persistance.model.ClientPartenaire;

/**
 * Created by Hassine on 15	/02/2020.
 */
@Repository
public interface IClientPartnerRepository extends MongoRepository<ClientPartenaire,String> {
    public ClientPartenaire findByQrCodePartnAndIdPartenaire(String qrCode, String idPartenaire);    
    public ClientPartenaire findByIdClientPartenaire(String idClientPartenaire);
    public ClientPartenaire findByIdClientAndIdPartenaire(String idClient, String idPartenaire);
    public ClientPartenaire findByIdClientPartenaireAndIdPartenaire(String idClientParttenaire, String idPartenaire); 
    public List<ClientPartenaire> findAllByidClient(String idClientBprice);  
    public List<ClientPartenaire> findAllByIdPartenaire(String idPartenaire);
    public List<ClientPartenaire> findAllByIdPartenaireAndIsActif(String idPartenaire,short factif);
    public ClientPartenaire findByIdClient(String idClientBprice); 
    public List<ClientPartenaire> findAllByIdPartenaireAndIdJournee(String idPartenaire,String idJournee);
    public List<ClientPartenaire> findAllByIdPartenaireAndIsActifAndIsconnected(String idPartenaire,short factif,short isConnected );
    public List<ClientPartenaire> findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire(String idPartenaire,short factif,String idgroupe);

}

