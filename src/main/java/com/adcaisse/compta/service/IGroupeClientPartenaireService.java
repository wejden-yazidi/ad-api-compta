package com.adcaisse.compta.service;

import com.adcaisse.compta.response.ResponseObject;
import com.bprice.persistance.model.Client;
import com.bprice.persistance.model.GroupeClientPartenaire;

public interface IGroupeClientPartenaireService {
    public ResponseObject CreateGroupeClientPartenaire(GroupeClientPartenaire groupeClientPartenaire);
    public ResponseObject actifGroupeClientPartenaire(String idgroupeClientPartenaire,Short type);
    public ResponseObject findAllByIdPartenaireOrderByDateCreationDesc(String idpartenaire);
}
