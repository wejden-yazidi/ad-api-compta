package com.adcaisse.compta.service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adcaisse.compta.Enum.EnumMessage;
import com.adcaisse.compta.repository.IClientBpriceRepository;
import com.adcaisse.compta.repository.IClientPartnerRepository;
import com.adcaisse.compta.repository.IMvtSoldeRepository;
import com.adcaisse.compta.response.ResponseObject;
import com.adcaisse.compta.service.IMvtSoldeService;
import com.bprice.persistance.model.ClientPartenaire;
import com.bprice.persistance.model.MvtSolde;

@Service
public class MvtSoldeServiceImpl implements IMvtSoldeService {
    
	@Autowired
    private IClientBpriceRepository clientRepository;
   
    @Autowired
    private IClientPartnerRepository clientPartnerRepository;
    
    @Autowired
    private IMvtSoldeRepository mvtSoldeRepository;
    
        
    Logger logger = LoggerFactory.getLogger(MvtSoldeServiceImpl.class);

    
	
	@Override
	public ResponseObject CreateMvtSolde(MvtSolde mvtSolde) {
		logger.info("MvtSoldeServiceImpl : CreateMvtSolde : START");
		try{
			// check customer exist
			ClientPartenaire cltPart = clientPartnerRepository.findByIdClientPartenaire(mvtSolde.getIdClientPartenaire());
			if(null != cltPart){
				mvtSoldeRepository.save(mvtSolde);
				return new ResponseObject(EnumMessage.SUCCESS_CREATION.code, EnumMessage.SUCCESS_CREATION.label, mvtSolde);
			}else{				
				 logger.info("MvtSoldeServiceImpl : CreateMvtSolde : CLIENT INEXISTANT : ");
		    	 return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
			
		 }catch (Exception e){
	    	 logger.info("MvtSoldeServiceImpl : CreateMvtSolde : Exception : " + e.getMessage());
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
        }

	}


	


	@Override
	public ResponseObject getAllMvtSoldeByCustomer(String idClientPartner) {
		logger.info("MvtSoldeServiceImpl : getAllMvtSoldeByCustomer : START");
		try{
			// check customer exist
			ClientPartenaire cltPart = clientPartnerRepository.findByIdClientPartenaire(idClientPartner);
			if(null != cltPart){
				List<MvtSolde> listMvtSolde = mvtSoldeRepository.findAllByIdClientPartenaireOrderByDateOperationDesc(idClientPartner);
				if(listMvtSolde.size() > 0){
					return new ResponseObject(EnumMessage.LIST_OPERTATION_NOT_EMPTY.code, EnumMessage.LIST_OPERTATION_NOT_EMPTY.label, listMvtSolde);
				}else{
					return new ResponseObject(EnumMessage.LIST_OPERTATION_EMPTY.code, EnumMessage.LIST_OPERTATION_EMPTY.label, null);
				}
				
			}else{				
				 logger.info("MvtSoldeServiceImpl : CreateMvtSolde : CLIENT INEXISTANT : ");
		    	 return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
			
		 }catch (Exception e){
	    	 logger.info("MvtSoldeServiceImpl : getAllMvtSoldeByCustomer : Exception : " + e.getMessage());
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
        }
	}

	@Override
	public ResponseObject getMvtSoldeByIdMvt(String idMvtSolde) {
		logger.info("MvtSoldeServiceImpl : getMvtSoldeByIdMvt : START");
		try{
				MvtSolde mvtSolde = mvtSoldeRepository.findByIdMvtSolde(idMvtSolde);
				if(mvtSolde != null){
					return new ResponseObject(EnumMessage.MVT_SOLDE_EXIST.code, EnumMessage.MVT_SOLDE_EXIST.label, mvtSolde);
				}else{
					return new ResponseObject(EnumMessage.MVT_SOLDE_NOT_EXIST.code, EnumMessage.MVT_SOLDE_NOT_EXIST.label, null);
				}
			
		 }catch (Exception e){
	    	 logger.info("MvtSoldeServiceImpl : getAllMvtSoldeByCustomer : Exception : " + e.getMessage());
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
        }
	}
	
	
	
 
}
