package com.adcaisse.compta.repository;

import com.bprice.persistance.model.Client;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Hassine on 15/02/2020.
 */
@Repository
public interface IClientBpriceRepository extends MongoRepository<Client,String> {
    public Client findByQrCodeBprice(String qrCode);
    public Client findByNTel(String nTel);
    public Client findByIdClient(String idClient);     
    public List<Client> findAllByFActive(short factif);
    public List<Client> findAllByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNTelOrEmailContainingIgnoreCase(String name,String prenom,String tel,String email);
}
