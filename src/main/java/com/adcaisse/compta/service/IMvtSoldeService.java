package com.adcaisse.compta.service;

import com.adcaisse.compta.response.ResponseObject;
import com.bprice.persistance.model.MvtSolde;

public interface IMvtSoldeService {
	
	public ResponseObject CreateMvtSolde(MvtSolde mvtSolde);
    public ResponseObject getMvtSoldeByIdMvt(String IdMvtSolde);
    public ResponseObject getAllMvtSoldeByCustomer(String idClientPartner);
}
