package com.adcaisse.compta.service;

import com.adcaisse.compta.dto.ClientDtoForUpdate;
import com.adcaisse.compta.dto.ClientPartnerAfterConnexionDto;
import com.adcaisse.compta.dto.ClientPartnerDto;
import com.adcaisse.compta.dto.TransfertDto;
import com.adcaisse.compta.response.ResponseObject;
import com.bprice.persistance.model.Client;
import com.bprice.persistance.model.ClientPartenaire;

public interface ICustomerService {
	
	    public ResponseObject CreateClientBprice(Client Client);
	    public ResponseObject CreateClientPartner(ClientPartenaire Client);
	    public ResponseObject blockClientBprice(String idClientBprice);
	    public ResponseObject blockClientPartner(String idClientBprice, short isActif);
	    public ResponseObject updateClientBpriceByAdmin(ClientDtoForUpdate clientDtoForUpdate);
	    public ResponseObject updateClientBpriceByPartner(ClientPartnerDto clientPartnerDto);
	    public ResponseObject findByIdClientBprice(String idClient);
	    public ResponseObject findCustomerByTelephone(String nTel, String idPartner);
	    public ResponseObject findClientPartnerByIdClient(String idClient, String idPartner);
	    public ResponseObject findAllClientByIdPartener(String idPartenaire);
	    public ResponseObject findAllClientBprice();
	    public ResponseObject findAllClientBpriceByStatus(Short factif);
	    public ResponseObject findAllActiveClientByPartenaire(String idPartenaire);
	    public ResponseObject searchClientbyIdPartenaire(String value, String idPartenaire);
	    public ResponseObject findClientByQrCodeBprice(String qrCodeBprice);
	    public ResponseObject findClientByQrCodePartnerAndIdPartner(String qrCodePartner, String idPartenaire);
	    public ResponseObject updateSoldeForClientBprice(String idClient, Double amount, String operation);
	    public ResponseObject updateSoldeForClientPartner(String idClientBprice, Double amount, String operation, String idPartner, String typeOperation);
	    public ResponseObject TransfertSoldeFromClientPartnerToClientBprice(String idClientPatner, Double amount);
	    public ResponseObject affectQrCodeToClientPartner(String QrCode, String idClientBprice);
	    public ResponseObject findClientByTelephoneForSpecificPartner(String nTel, String idPartner);
	    public ResponseObject findClientByIdJourneeForSpecificPartner(String idPartenaire,String idJournee);
	    public ResponseObject UpdateTelephoneCustomer(String idClientBprice, String nTel, String Token);
	    public ResponseObject TransfertSoldeBetweenTwoClient(TransfertDto transfertDto);
	    public ResponseObject loginByPhone(String phone, String password, String idPartenaire);
	    public ResponseObject loginByFidCard(String qrCode,String idPartenaire);
	    public ResponseObject updateClientPartnerAfterConnexion(ClientPartnerAfterConnexionDto clientPartnerAfterConnexionDto);
	    public ResponseObject TransfertSoldeFromPartnerToClientPartner(TransfertDto transfertDto);
	    public ResponseObject findByIdClientPartner(String idClientPartner);
	    
	    public ResponseObject findAllActiveAndConnectedClientByPartenaire(String idPartenaire);
	    
	    public ResponseObject resendPassword(String phone, String idPartenaire) ;
		public ResponseObject findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire(String idpartenaire, Short isActif,String idgroupe);
		public ResponseObject affecteclienttogroupe(String idpartenaire,String idgroupe);
		public ResponseObject updateMyClientPartnerInformations(ClientPartnerDto clientPartnerDto);


}
