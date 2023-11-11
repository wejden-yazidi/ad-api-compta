package com.adcaisse.compta.service.Impl;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adcaisse.compta.Enum.EnumMessage;
import com.adcaisse.compta.dto.CadeauDto;
import com.adcaisse.compta.dto.ClientDtoForUpdate;
import com.adcaisse.compta.dto.ClientPartnerAfterConnexionDto;
import com.adcaisse.compta.dto.ClientPartnerDto;
import com.adcaisse.compta.dto.ClientResponseDto;
import com.adcaisse.compta.dto.Etat;
import com.adcaisse.compta.dto.NotificationDto;
import com.adcaisse.compta.dto.TransfertDto;
import com.adcaisse.compta.repository.IClientBpriceRepository;
import com.adcaisse.compta.repository.IClientPartnerRepository;
import com.adcaisse.compta.repository.IGroupeClientPartenaireRepository;
import com.adcaisse.compta.repository.IPartenaireBpriceRepository;
import com.adcaisse.compta.response.ResponseObject;
import com.adcaisse.compta.service.CheckNotificationService;
import com.adcaisse.compta.service.CheckOtpService;
import com.adcaisse.compta.service.CheckWelcomeGift;
import com.adcaisse.compta.service.ICustomerService;
import com.adcaisse.compta.service.IGroupeClientPartenaireService;
import com.adcaisse.compta.service.IMvtSoldeService;
import com.adcaisse.compta.util.CryptDecyp;
import com.adcaisse.compta.util.QrGenerator;
import com.bprice.persistance.model.Cadeau;
import com.bprice.persistance.model.Client;
import com.bprice.persistance.model.ClientPartenaire;
import com.bprice.persistance.model.MvtSolde;
import com.bprice.persistance.model.NotifClientPartner;
import com.bprice.persistance.model.Notification;
import com.bprice.persistance.model.OneTimePwd;
import com.bprice.persistance.model.PartenaireBprice;


@Service
public class CustomerServiceImpl implements ICustomerService {
    
	@Autowired
    private IClientBpriceRepository clientRepository;
   
    @Autowired
    private IClientPartnerRepository clientPartnerRepository;
    
    @Autowired
    private CheckOtpService checkOtpService;
    
    @Autowired
    private IMvtSoldeService mvtSoldeService;
    
    @Autowired
    private CheckNotificationService checkNotificationService;
    
    @Autowired
    IGroupeClientPartenaireRepository groupeClientPartenaireRepository;
    
    @Autowired
    IPartenaireBpriceRepository iPartenaireBpriceRepository;
    
    @Autowired
	private CheckWelcomeGift checkWelcomeGift;
    
    @Value("${bprice.idApplicationCaisse}")
	private String idApplicationCaisse;
    
    @Value("${bprice.idApplicationMobile}")
	private String idApplicationMobile;
    
    Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    
	@Override
	public ResponseObject CreateClientBprice(Client client) {
		
		logger.info("CustomerServiceImpl : CreateClientBprice : START - INPUT : client : " + client.getIdClient());
		//generation d'un QR CODE en backgroud
		String qrCode = QrGenerator.generateQrCode(10);
		try{
			//find Customer By Telephone
			Client clt = clientRepository.findByNTel(client.getnTel());
			logger.info("CustomerServiceImpl : CreateClientBprice : recherche client par TEL" + client.getnTel());
			if(clt != null){
				//client existe avec son téléphone
				logger.info("CustomerServiceImpl : CreateClientBprice : client existant par tél : " + client.getIdClient());
				return new ResponseObject(EnumMessage.SUCCESS_CREATION.code, EnumMessage.SUCCESS_CREATION.label, clt);
			}else{
				Client clientBprice = client;
				clientBprice.setQrCodeBprice(qrCode);
				clientBprice = clientRepository.save(client);
				logger.info("CustomerServiceImpl : CreateClientBprice : nouvelle création de client : " + client.getIdClient());
				return new ResponseObject(EnumMessage.SUCCESS_CREATION.code, EnumMessage.SUCCESS_CREATION.label, clientBprice);
			}
			
	     }catch (Exception e){
	    	 logger.info("CustomerServiceImpl : CreateClientBprice : Exception : " + e.getMessage());
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
     }
	}
	
	
	@Override
	public ResponseObject CreateClientPartner(ClientPartenaire clientPartenaire) {
		// this operation assume that a customer bprice exist yet in our DB.
		logger.info("CustomerServiceImpl : CreateClientPartner : START - INPUT : clientPartenaire : " + clientPartenaire.getIdClient());
		try{
			//check if customer exist in the table ClientPartner
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientAndIdPartenaire(clientPartenaire.getIdClient(), clientPartenaire.getIdPartenaire());
			if(null != cltPartner){
				logger.info("CustomerServiceImpl : CreateClientPartner : Client existe dans la table partenaire : " + cltPartner.getIdClientPartenaire());
				return new ResponseObject(EnumMessage.CLIENT_EXIST.code, EnumMessage.CLIENT_EXIST.label, cltPartner);
			}else{
								
				ClientPartenaire clientPartner = clientPartnerRepository.save(clientPartenaire);
				
				// check if partner permet welcome gift
	        	CadeauDto cadeauDto = new CadeauDto();
	        	cadeauDto.setDate(new Date());
	        	cadeauDto.setIdparteanire(clientPartenaire.getIdPartenaire());
	        	cadeauDto.setType("Bienvenue");
	        	
	        	try {
					Cadeau cadeau = checkWelcomeGift.checkWelcomeGiftService(cadeauDto);
					if(cadeau != null){
						// le cadeau existe, faire une alimentation du solde et envoyer une notification
						ResponseObject response = updateSoldeForClientPartner(clientPartenaire.getIdClientPartenaire(), cadeau.getValeur(), "+",clientPartenaire.getIdPartenaire(), "Cadeau Bienvenue");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return new ResponseObject(-1, "Exception in checkWelcomeGift ", null);
				}	        
				
				logger.info("CustomerServiceImpl : CreateClientPartner : nouvelle création de clientPartenaire : " + clientPartenaire.getIdClient());
				return new ResponseObject(EnumMessage.SUCCESS_CREATION.code, EnumMessage.SUCCESS_CREATION.label, clientPartner);
			}			
			
		}catch (Exception e){
			 logger.info("CustomerServiceImpl : CreateClientPartner : Exception : " + e.getMessage());
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
	}
		
	@Override
	public ResponseObject blockClientBprice(String idClientBprice) {
		logger.info("CustomerServiceImpl : blockClientBprice : START - INPUT : idClient : " + idClientBprice);

		try{			
			Client cltBprice = clientRepository.findByIdClient(idClientBprice);
			logger.info("CustomerServiceImpl : blockClientBprice : findByIdClient : idClient" + cltBprice.getIdClient());				
				if(null != cltBprice){
					cltBprice.setfActive((short) 0);
					clientRepository.save(cltBprice);
					logger.info("CustomerServiceImpl : blockClientBprice : client Bloqué avec succès : idClient" + cltBprice.getIdClient());
					return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, cltBprice);
				}else{
					logger.info("CustomerServiceImpl : blockClientBprice : " + cltBprice.getIdClient());
					return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
				}
			
		}catch (Exception e){
			logger.info("CustomerServiceImpl : blockClientBprice : Exception : " + e.getMessage());
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
	}

@Override
	public ResponseObject blockClientPartner(String idClientPartner, short isActif) {
		try{			
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientPartenaire(idClientPartner);
			if(null != cltPartner){
				cltPartner.setIsActif(isActif);
				clientPartnerRepository.save(cltPartner);
				return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, cltPartner);
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	
	
	@Override
	public ResponseObject updateClientBpriceByAdmin(ClientDtoForUpdate clientDtoForUpdate) {
		try{			
				Client cltBprice = clientRepository.findByIdClient(clientDtoForUpdate.getIdClient());
				if(null != cltBprice){
					if(clientDtoForUpdate.getNom() != null && !clientDtoForUpdate.getNom().equals("")){
						cltBprice.setNom(clientDtoForUpdate.getNom());
					}
					if(clientDtoForUpdate.getPrenom() != null && !clientDtoForUpdate.getPrenom().equals("")){
						cltBprice.setPrenom(clientDtoForUpdate.getNom());
					}
					if (clientDtoForUpdate.getDateNaissance() != null && !clientDtoForUpdate.getDateNaissance().equals("")){
						cltBprice.setDateNaissance(clientDtoForUpdate.getDateNaissance());
					}
			    		
			    	if (clientDtoForUpdate.getEmail() != null && !clientDtoForUpdate.getEmail().equals("")){
			    		cltBprice.setEmail(clientDtoForUpdate.getEmail());
					}
			    	
			    	if (clientDtoForUpdate.getGenre() != null && !clientDtoForUpdate.getGenre().equals("")){
			    		cltBprice.setGenre(clientDtoForUpdate.getGenre());
					}
			    	
			    	if (clientDtoForUpdate.getMatfiscale() != null && !clientDtoForUpdate.getMatfiscale().equals("")){
			    		cltBprice.setMatfiscale(clientDtoForUpdate.getMatfiscale());
					}   
			    	
			    	if (clientDtoForUpdate.getRaisonsocial() != null && !clientDtoForUpdate.getRaisonsocial().equals("")){
			    		cltBprice.setRaisonsocial(clientDtoForUpdate.getRaisonsocial());
					}   
			    	
			    	if (clientDtoForUpdate.getNumpatente() != null && !clientDtoForUpdate.getNumpatente().equals("")){
			    		cltBprice.setNumpatente(clientDtoForUpdate.getNumpatente());
					}  
			    	
			    	if (clientDtoForUpdate.getSiegesocial() != null && !clientDtoForUpdate.getSiegesocial().equals("")){
			    		cltBprice.setSiegesocial(clientDtoForUpdate.getSiegesocial());
					} 
			    	
			    	if (clientDtoForUpdate.getAdress() != null && !clientDtoForUpdate.getAdress().equals("")){
			    		cltBprice.setAdress(clientDtoForUpdate.getAdress());
					} 
			    	
					
					clientRepository.save(cltBprice);
					return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, cltBprice);
								
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	
	public ResponseObject updateClientBpriceByPartner(ClientPartnerDto clientPartnerDto){
		
		try{			
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientPartenaireAndIdPartenaire(clientPartnerDto.getIdClient(), clientPartnerDto.getIdPartenaire());
			if(null != cltPartner){	
				
				if(clientPartnerDto.getNom() != null && !clientPartnerDto.getNom().equals("")){
					cltPartner.setNom(clientPartnerDto.getNom());
				}
				if(clientPartnerDto.getPrenom() != null && !clientPartnerDto.getPrenom().equals("")){
					cltPartner.setPrenom(clientPartnerDto.getPrenom());
				}	
				
				if(clientPartnerDto.getIdGroupeClient() != null && !clientPartnerDto.getIdGroupeClient().equals("")){
					cltPartner.setIdGroupeClientPartenaire(clientPartnerDto.getIdGroupeClient());
				}	
				
				if(clientPartnerDto.getMatfiscale() != null && !clientPartnerDto.getMatfiscale().equals("")){
					cltPartner.setMatfiscale(clientPartnerDto.getMatfiscale());
				}	
				
				if(clientPartnerDto.getRaisonsocial() != null && !clientPartnerDto.getRaisonsocial().equals("")){
					cltPartner.setRaisonsocial(clientPartnerDto.getRaisonsocial());
				}
				
				if(clientPartnerDto.getAdress() != null && !clientPartnerDto.getAdress().equals("")){
					cltPartner.setAdress(clientPartnerDto.getAdress());
				}
				
				
				clientPartnerRepository.save(cltPartner);
				return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label,cltPartner);
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}				
	}
	
	
	@Override
	public ResponseObject findByIdClientBprice(String idClient) {
		try{			
	
			Client client = clientRepository.findByIdClient(idClient);
			if(null != client){
				return new ResponseObject(EnumMessage.CLIENT_EXIST.code, EnumMessage.CLIENT_EXIST.label, client);
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
	}
	
	@Override
	public ResponseObject findByIdClientPartner(String idClientpartner) {
		try{			
	
			ClientPartenaire client = clientPartnerRepository.findByIdClientPartenaire(idClientpartner);
			if(null != client){
				return new ResponseObject(EnumMessage.CLIENT_EXIST.code, EnumMessage.CLIENT_EXIST.label, client);
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
	}
	@Override
	public ResponseObject findCustomerByTelephone(String nTel, String idPartner) {
		try{			
			
			Client client = clientRepository.findByNTel(nTel);
			if(null != client ){
				ClientPartenaire clientPartner = clientPartnerRepository.findByIdClientAndIdPartenaire(client.getIdClient(), idPartner );
				if(null != clientPartner && clientPartner.getIsActif() == (short) 1){	
					ClientResponseDto clientResponseDto = new ClientResponseDto();
					clientResponseDto.setNom(clientPartner.getNom());
					clientResponseDto.setPrenom(clientPartner.getPrenom());
					clientResponseDto.setQrCodePartn(clientPartner.getQrCodePartn());
					clientResponseDto.setSoldePartn(clientPartner.getSoldePartn());
					clientResponseDto.setGenre(client.getGenre());
					clientResponseDto.setDateNaissance(client.getDateNaissance());
					clientResponseDto.setnTel(client.getnTel());
					clientResponseDto.setEmail(client.getEmail());
					clientResponseDto.setTokenNotification(clientPartner.getTokenNotification());
					clientResponseDto.setPassword(clientPartner.getPassword());
					clientResponseDto.setIsconnected(clientPartner.getIsconnected());
					clientResponseDto.setIdPartenaire(clientPartner.getIdPartenaire());
					clientResponseDto.setIdClientPartenaire(clientPartner.getIdClientPartenaire());
					clientResponseDto.setIdClient(clientPartner.getIdClient());
					clientResponseDto.setDateCreation(clientPartner.getDateCreation());
					clientResponseDto.setIsconnected(clientPartner.getIsconnected());
					clientResponseDto.setIdGroupeClient(clientPartner.getIdGroupeClientPartenaire());
					
					clientResponseDto.setMatfiscale(clientPartner.getMatfiscale());		
					clientResponseDto.setRaisonsocial(clientPartner.getRaisonsocial());	
					clientResponseDto.setNumpatente(client.getNumpatente());
					clientResponseDto.setSiegesocial(client.getSiegesocial());
					clientResponseDto.setAdress(clientPartner.getAdress());
					clientResponseDto.setImage(clientPartner.getImage());
					
					return new ResponseObject(EnumMessage.CLIENT_EXIST.code, EnumMessage.CLIENT_EXIST.label, clientResponseDto);
				}else{
					return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
				}
				
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
	}
	@Override
	public ResponseObject findClientPartnerByIdClient(String idClientBprice, String idPartner) {
		
		try{			
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientPartenaireAndIdPartenaire(idClientBprice, idPartner);
			if(null != cltPartner){
				Client cltBprice = clientRepository.findByIdClient(cltPartner.getIdClient());
				if(null != cltBprice){					
						ClientResponseDto clientResponseDto = new ClientResponseDto();
						clientResponseDto.setNom(cltPartner.getNom());
						clientResponseDto.setPrenom(cltPartner.getPrenom());
						clientResponseDto.setQrCodePartn(cltPartner.getQrCodePartn());
						clientResponseDto.setSoldePartn(cltPartner.getSoldePartn());
						clientResponseDto.setGenre(cltBprice.getGenre());
						clientResponseDto.setDateNaissance(cltBprice.getDateNaissance());
						clientResponseDto.setnTel(cltBprice.getnTel());
						clientResponseDto.setEmail(cltBprice.getEmail());
						clientResponseDto.setIdPartenaire(cltPartner.getIdClientPartenaire());
						clientResponseDto.setIdClientPartenaire(cltPartner.getIdClientPartenaire());
						clientResponseDto.setIdClient(cltPartner.getIdClient());
						clientResponseDto.setDateCreation(cltPartner.getDateCreation());	
						clientResponseDto.setIsconnected(cltPartner.getIsconnected());
						clientResponseDto.setIsActive(cltPartner.getIsActif());
						clientResponseDto.setIdGroupeClient(cltPartner.getIdGroupeClientPartenaire());
						
						clientResponseDto.setMatfiscale(cltPartner.getMatfiscale());		
						clientResponseDto.setRaisonsocial(cltPartner.getRaisonsocial());	
						clientResponseDto.setNumpatente(cltBprice.getNumpatente());
						clientResponseDto.setSiegesocial(cltBprice.getSiegesocial());
						clientResponseDto.setAdress(cltPartner.getAdress());
						clientResponseDto.setImage(cltPartner.getImage());
						
						return new ResponseObject(EnumMessage.CLIENT_EXIST.code, EnumMessage.CLIENT_EXIST.label, clientResponseDto);
				}else{					
					return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, cltBprice);
				}
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
	}
	
	
	@Override
	public ResponseObject findAllClientByIdPartener(String idPartenaire) {
		List<ClientPartenaire> listeClientPartner = new ArrayList<ClientPartenaire>();
		List<ClientResponseDto> listeClientOutput = new ArrayList<ClientResponseDto>();
		try{			
			listeClientPartner= clientPartnerRepository.findAllByIdPartenaire(idPartenaire);
			if(listeClientPartner.size() >0){
				for(ClientPartenaire clientPartenaire : listeClientPartner){
					Client cltBprice = clientRepository.findByIdClient(clientPartenaire.getIdClient());
					if(null != cltBprice){
						ClientResponseDto clientResponseDto = new ClientResponseDto();
						clientResponseDto.setNom(clientPartenaire.getNom());
						clientResponseDto.setPrenom(clientPartenaire.getPrenom());
						clientResponseDto.setQrCodePartn(clientPartenaire.getQrCodePartn());
						clientResponseDto.setSoldePartn(clientPartenaire.getSoldePartn());
						clientResponseDto.setGenre(cltBprice.getGenre());
						clientResponseDto.setDateNaissance(cltBprice.getDateNaissance());
						clientResponseDto.setnTel(cltBprice.getnTel());
						clientResponseDto.setEmail(cltBprice.getEmail());
						clientResponseDto.setIdPartenaire(idPartenaire);
						clientResponseDto.setIdClientPartenaire(clientPartenaire.getIdClientPartenaire());
						clientResponseDto.setIdClient(clientPartenaire.getIdClient());
						clientResponseDto.setDateCreation(clientPartenaire.getDateCreation());	
						clientResponseDto.setIsconnected(clientPartenaire.getIsconnected());
						clientResponseDto.setIsActive(clientPartenaire.getIsActif());
						clientResponseDto.setIdGroupeClient(clientPartenaire.getIdGroupeClientPartenaire());
						clientResponseDto.setTokenNotification(clientPartenaire.getTokenNotification());
						clientResponseDto.setMatfiscale(clientPartenaire.getMatfiscale());		
						clientResponseDto.setRaisonsocial(clientPartenaire.getRaisonsocial());	
						clientResponseDto.setNumpatente(cltBprice.getNumpatente());
						clientResponseDto.setSiegesocial(cltBprice.getSiegesocial());
						clientResponseDto.setAdress(clientPartenaire.getAdress());
						clientResponseDto.setImage(clientPartenaire.getImage());
						
						listeClientOutput.add(clientResponseDto);
					}
				}
				return new ResponseObject(EnumMessage.LIST_CLIENT_NOT_EMPTY.code, EnumMessage.LIST_CLIENT_NOT_EMPTY.label, listeClientOutput);
			}else{
				return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	
	@Override
	public ResponseObject findAllClientBprice() {
		List<Client> listeClient = new ArrayList<Client>();
		try{			
			listeClient=clientRepository.findAll();
			if(listeClient.size() >0){
				return new ResponseObject(EnumMessage.LIST_CLIENT_NOT_EMPTY.code, EnumMessage.LIST_CLIENT_NOT_EMPTY.label, listeClient);
			}else{
				return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
		
	}
	@Override
	public ResponseObject findAllClientBpriceByStatus(Short factif) {
		List<Client> listeClient = new ArrayList<Client>();
		try{			
			listeClient=clientRepository.findAllByFActive(factif);
			if(listeClient.size() >0){
				return new ResponseObject(EnumMessage.LIST_CLIENT_NOT_EMPTY.code, EnumMessage.LIST_CLIENT_NOT_EMPTY.label, listeClient);
			}else{
				return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	@Override
	public ResponseObject findAllActiveClientByPartenaire(String idPartenaire) {
		List<ClientPartenaire> listeClientPartner = new ArrayList<ClientPartenaire>();
		List<ClientResponseDto> listeClientOutput = new ArrayList<ClientResponseDto>();

	try{			
			listeClientPartner= clientPartnerRepository.findAllByIdPartenaireAndIsActif(idPartenaire, (short) 1);
			if(listeClientPartner.size() >0){
				for(ClientPartenaire clientPartenaire : listeClientPartner){
					Client cltBprice = clientRepository.findByIdClient(clientPartenaire.getIdClient());
					logger.info("findAllActiveClientByPartenaire idClient " +   cltBprice.getIdClient());
					if(null != cltBprice){
						ClientResponseDto clientResponseDto = new ClientResponseDto();
						clientResponseDto.setNom(clientPartenaire.getNom());
						clientResponseDto.setPrenom(clientPartenaire.getPrenom());
						clientResponseDto.setQrCodePartn(clientPartenaire.getQrCodePartn());
						clientResponseDto.setSoldePartn(clientPartenaire.getSoldePartn());
						clientResponseDto.setGenre(cltBprice.getGenre());
						clientResponseDto.setDateNaissance(cltBprice.getDateNaissance());
						clientResponseDto.setnTel(cltBprice.getnTel());
						clientResponseDto.setEmail(cltBprice.getEmail());
						clientResponseDto.setIdPartenaire(idPartenaire);
						clientResponseDto.setIdClientPartenaire(clientPartenaire.getIdClientPartenaire());
						clientResponseDto.setIdClient(clientPartenaire.getIdClient());
						clientResponseDto.setDateCreation(clientPartenaire.getDateCreation());	
						clientResponseDto.setIsconnected(clientPartenaire.getIsconnected());
						clientResponseDto.setIsActive(clientPartenaire.getIsActif());
						clientResponseDto.setIdGroupeClient(clientPartenaire.getIdGroupeClientPartenaire());
						clientResponseDto.setTokenNotification(clientPartenaire.getTokenNotification());
						clientResponseDto.setMatfiscale(clientPartenaire.getMatfiscale());		
						clientResponseDto.setRaisonsocial(clientPartenaire.getRaisonsocial());	
						clientResponseDto.setNumpatente(cltBprice.getNumpatente());
						clientResponseDto.setSiegesocial(cltBprice.getSiegesocial());
						clientResponseDto.setAdress(clientPartenaire.getAdress());
						clientResponseDto.setImage(clientPartenaire.getImage());
						
						listeClientOutput.add(clientResponseDto);
						if(cltBprice.getDateCreation()!=null)
						if(cltBprice.getDateCreation()!=null)
						listeClientOutput.sort(Comparator.comparing(ClientResponseDto -> clientResponseDto.getDateCreation()));
						
					}
				}
				return new ResponseObject(EnumMessage.LIST_CLIENT_NOT_EMPTY.code, EnumMessage.LIST_CLIENT_NOT_EMPTY.label, listeClientOutput);
			}else{
				return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	
	@Override
	public ResponseObject findAllActiveAndConnectedClientByPartenaire(String idPartenaire) {
		List<ClientPartenaire> listeClientPartner = new ArrayList<ClientPartenaire>();
		List<ClientResponseDto> listeClientOutput = new ArrayList<ClientResponseDto>();

	try{			
			listeClientPartner= clientPartnerRepository.findAllByIdPartenaireAndIsActifAndIsconnected(idPartenaire, (short) 1, (short) 1);
			if(listeClientPartner.size() >0){
				for(ClientPartenaire clientPartenaire : listeClientPartner){
					Client cltBprice = clientRepository.findByIdClient(clientPartenaire.getIdClient());
					if(null != cltBprice){
						ClientResponseDto clientResponseDto = new ClientResponseDto();
						clientResponseDto.setNom(clientPartenaire.getNom());
						clientResponseDto.setPrenom(clientPartenaire.getPrenom());
						clientResponseDto.setQrCodePartn(clientPartenaire.getQrCodePartn());
						clientResponseDto.setSoldePartn(clientPartenaire.getSoldePartn());
						clientResponseDto.setGenre(cltBprice.getGenre());
						clientResponseDto.setDateNaissance(cltBprice.getDateNaissance());
						clientResponseDto.setnTel(cltBprice.getnTel());
						clientResponseDto.setEmail(cltBprice.getEmail());
						clientResponseDto.setIdPartenaire(idPartenaire);
						clientResponseDto.setIdClientPartenaire(clientPartenaire.getIdClientPartenaire());
						clientResponseDto.setIdClient(clientPartenaire.getIdClient());
						clientResponseDto.setDateCreation(clientPartenaire.getDateCreation());	
						clientResponseDto.setIsconnected(clientPartenaire.getIsconnected());
						clientResponseDto.setIsActive(clientPartenaire.getIsActif());
						clientResponseDto.setIdGroupeClient(clientPartenaire.getIdGroupeClientPartenaire());
						clientResponseDto.setTokenNotification(clientPartenaire.getTokenNotification());
						clientResponseDto.setMatfiscale(clientPartenaire.getMatfiscale());		
						clientResponseDto.setRaisonsocial(clientPartenaire.getRaisonsocial());	
						clientResponseDto.setNumpatente(cltBprice.getNumpatente());
						clientResponseDto.setSiegesocial(cltBprice.getSiegesocial());
						clientResponseDto.setAdress(clientPartenaire.getAdress());
						clientResponseDto.setImage(clientPartenaire.getImage());
						
						listeClientOutput.add(clientResponseDto);
						if(cltBprice.getDateCreation()!=null)
						listeClientOutput.sort(Comparator.comparing(ClientResponseDto -> clientResponseDto.getDateCreation()));
					}
				}
				return new ResponseObject(EnumMessage.LIST_CLIENT_NOT_EMPTY.code, EnumMessage.LIST_CLIENT_NOT_EMPTY.label, listeClientOutput);
			}else{
				return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
		
	}
	
	@Override
	public ResponseObject searchClientbyIdPartenaire(String value, String idPartenaire) {
		
		List<Client> listeClientInitiale = new ArrayList<Client>();
		List<ClientResponseDto> listeClientRelatedToPartner = new ArrayList<ClientResponseDto>();
		try{			
			listeClientInitiale=clientRepository.findAllByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNTelOrEmailContainingIgnoreCase(value, value, value, value);
			if(listeClientInitiale.size() >0){
				for (Client cltBprice : listeClientInitiale){
					ClientPartenaire clientPartner = clientPartnerRepository.findByIdClientAndIdPartenaire(cltBprice.getIdClient(), idPartenaire);
					if(null != clientPartner && clientPartner.getIsActif() == (short) 1){	
						ClientResponseDto clientResponseDto = new ClientResponseDto();
						clientResponseDto.setNom(clientPartner.getNom());
						clientResponseDto.setPrenom(clientPartner.getPrenom());
						clientResponseDto.setQrCodePartn(clientPartner.getQrCodePartn());
						clientResponseDto.setSoldePartn(clientPartner.getSoldePartn());
						clientResponseDto.setGenre(cltBprice.getGenre());
						clientResponseDto.setDateNaissance(cltBprice.getDateNaissance());
						clientResponseDto.setnTel(cltBprice.getnTel());
						clientResponseDto.setEmail(cltBprice.getEmail());
						clientResponseDto.setIdPartenaire(idPartenaire);
						clientResponseDto.setIdClientPartenaire(clientPartner.getIdClientPartenaire());
						clientResponseDto.setIdClient(clientPartner.getIdClient());
						clientResponseDto.setDateCreation(clientPartner.getDateCreation());	
						clientResponseDto.setIsActive(clientPartner.getIsActif());
						clientResponseDto.setIsconnected(clientPartner.getIsconnected());
						clientResponseDto.setIdGroupeClient(clientPartner.getIdGroupeClientPartenaire());
						
						clientResponseDto.setMatfiscale(clientPartner.getMatfiscale());		
						clientResponseDto.setRaisonsocial(clientPartner.getRaisonsocial());	
						clientResponseDto.setNumpatente(cltBprice.getNumpatente());
						clientResponseDto.setSiegesocial(cltBprice.getSiegesocial());
						clientResponseDto.setAdress(clientPartner.getAdress());
						clientResponseDto.setImage(clientPartner.getImage());
						
						listeClientRelatedToPartner.add(clientResponseDto);
						if(cltBprice.getDateCreation()!=null)
						listeClientRelatedToPartner.sort(Comparator.comparing(ClientResponseDto -> clientResponseDto.getDateCreation()));
					}
				}
				if(listeClientRelatedToPartner.size()> 0){
					return new ResponseObject(EnumMessage.LIST_CLIENT_NOT_EMPTY.code, EnumMessage.LIST_CLIENT_NOT_EMPTY.label, listeClientRelatedToPartner);
				}else{
					return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
				}
			}else{
				return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
	}
	
	@Override
	public ResponseObject findClientByQrCodeBprice(String qrCodeBprice) {
		
		try{			
			Client client = clientRepository.findByQrCodeBprice(qrCodeBprice);
			if(null != client){				
				return new ResponseObject(EnumMessage.CLIENT_EXIST.code, EnumMessage.CLIENT_EXIST.label, client);
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
		
	}
	
	
	@Override
	public ResponseObject findClientByQrCodePartnerAndIdPartner(String qrCodePartner, String idPartenaire) {
		try{			
			ClientPartenaire clientPartner = clientPartnerRepository.findByQrCodePartnAndIdPartenaire(qrCodePartner, idPartenaire);
			if(null != clientPartner && clientPartner.getIsActif() == (short) 1){	
				Client client = clientRepository.findByIdClient(clientPartner.getIdClient());
				ClientResponseDto clientResponseDto = new ClientResponseDto();
				clientResponseDto.setNom(clientPartner.getNom());
				clientResponseDto.setPrenom(clientPartner.getPrenom());
				clientResponseDto.setQrCodePartn(clientPartner.getQrCodePartn());
				clientResponseDto.setSoldePartn(clientPartner.getSoldePartn());
				clientResponseDto.setGenre(client.getGenre());
				clientResponseDto.setDateNaissance(client.getDateNaissance());
				clientResponseDto.setnTel(client.getnTel());
				clientResponseDto.setEmail(client.getEmail());
				clientResponseDto.setTokenNotification(clientPartner.getTokenNotification());
				clientResponseDto.setPassword(clientPartner.getPassword());
				clientResponseDto.setIsconnected(clientPartner.getIsconnected());
				clientResponseDto.setIdPartenaire(clientPartner.getIdPartenaire());
				clientResponseDto.setIdClientPartenaire(clientPartner.getIdClientPartenaire());
				clientResponseDto.setIdClient(clientPartner.getIdClient());
				clientResponseDto.setDateCreation(clientPartner.getDateCreation());
				clientResponseDto.setIsconnected(clientPartner.getIsconnected());
				clientResponseDto.setIsActive(clientPartner.getIsActif());
				clientResponseDto.setIdGroupeClient(clientPartner.getIdGroupeClientPartenaire());
				clientResponseDto.setMatfiscale(clientPartner.getMatfiscale());		
				clientResponseDto.setRaisonsocial(clientPartner.getRaisonsocial());	
				clientResponseDto.setNumpatente(client.getNumpatente());
				clientResponseDto.setSiegesocial(client.getSiegesocial());
				clientResponseDto.setAdress(clientPartner.getAdress());
				clientResponseDto.setImage(clientPartner.getImage());
				
				return new ResponseObject(EnumMessage.CLIENT_EXIST.code, EnumMessage.CLIENT_EXIST.label, clientResponseDto);
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	@Override
	public ResponseObject updateSoldeForClientBprice(String idClient, Double amount, String operation) {
		try{			
			Client cltBprice = clientRepository.findByIdClient(idClient);
				if(null != cltBprice){
					if(operation.equals("+")){
						cltBprice.setSoldeBprice(cltBprice.getSoldeBprice() + amount);
					}else{
						cltBprice.setSoldeBprice(cltBprice.getSoldeBprice() - amount);
					}
					clientRepository.save(cltBprice);
					return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, cltBprice);
				}else{
					return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
				}
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	@Override
	public ResponseObject updateSoldeForClientPartner(String idClientPartner, Double amount, String operation, String idPartner, String typeOperation) {
		try{			
			
			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.CEILING);
			//get partner informations
			PartenaireBprice partner = iPartenaireBpriceRepository.findByIdPartenaire(idPartner);
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientPartenaireAndIdPartenaire(idClientPartner, idPartner);
			if(null != cltPartner){
				String desc = "";
				String body = "";
				String verbe = "";
				if(operation.equals("+")){
					cltPartner.setSoldePartn(Double.valueOf(df.format(cltPartner.getSoldePartn() + amount).replace(",", ".")));
					 desc = "Remboursement FID solde depuis partenaire Adcaisse : " + partner.getAbbreviation() + " vers " + cltPartner.getIdClientPartenaire() ;
					 body = "Votre solde FID a été crédité par le montant de " + df.format(amount).replace(",", ".") + " DT de la part de votre partenaire  Adcaisse "+ partner.getAbbreviation() ;
					 verbe = "Transfert";
				}else{
					cltPartner.setSoldePartn(Double.valueOf(df.format(cltPartner.getSoldePartn() - amount).replace(",", ".")));
					 desc = "Retrait depuis votre solde FID "+ cltPartner.getIdClientPartenaire() +" pour paiement de commande chez le partenaire : " + partner.getAbbreviation();
					 body = "Votre solde FID a été débité par le montant de " + df.format(amount).replace(",", ".") + " DT de la part de votre partenaire  Adcaisse : "+ partner.getAbbreviation() ;
					 verbe = "Retrait";

				}
				clientPartnerRepository.save(cltPartner);
				//create mouvement solde pour le client destination
				MvtSolde mvtSoldeDestination = new MvtSolde();
				mvtSoldeDestination.setIdClientPartenaire(cltPartner.getIdClientPartenaire());
				mvtSoldeDestination.setMontant(Float.valueOf(df.format(amount).replace(",", ".")));
				mvtSoldeDestination.setSens(operation);				
				mvtSoldeDestination.setSource(verbe + " solde FID par votre partenaire  Adcaisse : "+ partner.getAbbreviation() );	
				mvtSoldeDestination.setTypeOperation(typeOperation);
				mvtSoldeDestination.setDateOperation(new Date());
				mvtSoldeDestination.setTrace(desc);
				mvtSoldeService.CreateMvtSolde(mvtSoldeDestination);
				
				//send notification to destinataire
				
				
				NotificationDto notificationdto = new NotificationDto();
				notificationdto.setBody(body);
				notificationdto.setDateDebut(new Date());
				notificationdto.setDescription(verbe + " solde FID par vore partenaire  Adcaisse : "+ partner.getAbbreviation());
				notificationdto.setIdPartenaire(cltPartner.getIdPartenaire());
				notificationdto.setIsSlider((short) 0);
				notificationdto.setTitre(typeOperation);
				notificationdto.setTypeNotification(typeOperation);
				
				NotifClientPartner notif = checkNotificationService.sendSingleNotification(notificationdto, cltPartner.getIdClientPartenaire());
				return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, cltPartner);
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	@Override
	public ResponseObject TransfertSoldeFromClientPartnerToClientBprice(String idClientPatner, Double amount) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public ResponseObject TransfertSoldeFromPartnerToClientPartner(TransfertDto transfertDto) {
		logger.info("CustomerServiceImpl : TransfertSoldeFromPartnerToClientPartner : START - INPUT : idPartner : " + transfertDto.getIdPartenaire() + " CLIENT DESTINATION :" + transfertDto.getIdClientPartnerCible() + " amount " + transfertDto.getAmount() );
		
		try{
			// get client Source
			ClientPartenaire cltPartnerDestination = clientPartnerRepository.findByIdClientPartenaireAndIdPartenaire(transfertDto.getIdClientPartnerCible(), transfertDto.getIdPartenaire());
			
			if(cltPartnerDestination ==null){
				
				logger.info("CustomerServiceImpl : TransfertSoldeFromPartnerToClientPartner : client not exist : " + transfertDto.getIdClientPartnerCible() );
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null );
			}
			
			
			if(cltPartnerDestination.getIsActif()==(short) 0){
				logger.info("CustomerServiceImpl : TransfertSoldeFromPartnerToClientPartner : the client Destination is not Actif : " + transfertDto.getIdClientPartnerCible() );
				return new ResponseObject(EnumMessage.CLIENT_EXIST_BLACKLISTED.code, EnumMessage.CLIENT_EXIST_BLACKLISTED.label,cltPartnerDestination );
			}
			
			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.CEILING);
			
						
			cltPartnerDestination.setSoldePartn(Double.valueOf(df.format(cltPartnerDestination.getSoldePartn() + transfertDto.getAmount()).replace(",", ".")));
			
			clientPartnerRepository.save(cltPartnerDestination);
			
			//get partner informations
			PartenaireBprice partner = iPartenaireBpriceRepository.findByIdPartenaire(cltPartnerDestination.getIdPartenaire());

			//create mouvement solde pour le client destination
			MvtSolde mvtSoldeDestination = new MvtSolde();
			mvtSoldeDestination.setIdClientPartenaire(transfertDto.getIdClientPartnerCible());
			mvtSoldeDestination.setMontant(Float.valueOf(df.format(transfertDto.getAmount()).replace(",", ".")));
			mvtSoldeDestination.setSens("+");
			mvtSoldeDestination.setSource("Transfert solde FID par votre partenaire  Adcaisse : "+ partner.getAbbreviation());	
			mvtSoldeDestination.setTypeOperation(transfertDto.getTypeOperation());
			mvtSoldeDestination.setDateOperation(new Date());
			mvtSoldeDestination.setTrace("Transfert solde depuis partenaire  Adcaisse : " + partner.getAbbreviation() + " vers " + cltPartnerDestination.getIdClientPartenaire());
			mvtSoldeService.CreateMvtSolde(mvtSoldeDestination);
			
			//send notification to destinataire
			
			NotificationDto notificationdto = new NotificationDto();
			notificationdto.setBody("Vous avez reçu le montant de " + df.format(transfertDto.getAmount()).replace(",", ".") + " DT de la part de votre partenaire  Adcaisse : " + partner.getAbbreviation()  );
			notificationdto.setDateDebut(new Date());
			notificationdto.setDescription("Transfert de solde depuis votre partenaire  Adcaisse : "+ partner.getAbbreviation() );
			notificationdto.setIdPartenaire(cltPartnerDestination.getIdPartenaire());
			notificationdto.setIsSlider((short) 0);
			notificationdto.setTitre(transfertDto.getTypeOperation());
			notificationdto.setTypeNotification(transfertDto.getTypeOperation());
			
			NotifClientPartner notif = checkNotificationService.sendSingleNotification(notificationdto, transfertDto.getIdClientPartnerCible());

			
			return new ResponseObject(EnumMessage.TRANSFERT_SUCCESS.code, EnumMessage.TRANSFERT_SUCCESS.label, mvtSoldeDestination);
			
	     }catch (Exception e){
	    	 logger.info("CustomerServiceImpl : TransfertSoldeFromPartnerToClientPartner : Exception : " + e.getMessage());
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
     }	
	}
	
	
	@Override
	public ResponseObject affectQrCodeToClientPartner(String qrCodePartner, String idClientBprice) {
		try{			
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClient(idClientBprice);
				if(null != cltPartner){
					cltPartner.setQrCodePartn(qrCodePartner);
					return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, cltPartner);
				}else{
					return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
				}
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}


	@Override
	public ResponseObject findClientByTelephoneForSpecificPartner(String nTel, String idPartner) {
		
		try{			
			Client client = clientRepository.findByNTel(nTel);
			if(null != client){
				ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientAndIdPartenaire(client.getIdClient(),idPartner );
				if(null != cltPartner){
//					client.setNom(cltPartner.getNom());
//					client.setPrenom(cltPartner.getPrenom());
//					client.setQrCodeBprice(cltPartner.getQrCodePartn());
//					client.setSoldeBprice(cltPartner.getSoldePartn());					
					return new ResponseObject(EnumMessage.CLIENT_EXIST.code, EnumMessage.CLIENT_EXIST.label, cltPartner);
				}else{
					return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
				}
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}


	@Override
	public ResponseObject findClientByIdJourneeForSpecificPartner(String idPartenaire, String idJournee) {
		List<ClientPartenaire> listClt = new ArrayList<ClientPartenaire>();
		
		try{			
			List<ClientPartenaire> listCltPartner = clientPartnerRepository.findAllByIdPartenaireAndIdJournee(idPartenaire, idJournee);
			if(null != listCltPartner && listCltPartner.size() > 0){
				for(ClientPartenaire cltPart : listCltPartner){
//					Client cltBprice = clientRepository.findByIdClient(cltPart.getIdClient());
//					cltBprice.setNom(cltPart.getNom());
//					cltBprice.setPrenom(cltPart.getPrenom());
//					cltBprice.setQrCodeBprice(cltPart.getQrCodePartn());
//					cltBprice.setSoldeBprice(cltPart.getSoldePartn());	
					
					listClt.add(cltPart);
				}
				if(listClt.size() > 0){
					return new ResponseObject(EnumMessage.LIST_CLIENT_NOT_EMPTY.code, EnumMessage.LIST_CLIENT_NOT_EMPTY.label, listClt);
				}else{
					return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
				}
				
			}else{
				return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
			}
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}


	@Override
	public ResponseObject UpdateTelephoneCustomer(String idClientPartner, String nTel, String token) {
		try{			
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientPartenaire(idClientPartner);
			
			
			if(null != cltPartner){
				
				Client clt = clientRepository.findByIdClient(cltPartner.getIdClient());
				//check token is valid
				Etat etat =  checkOtpService.checkOTP(cltPartner.getIdPartenaire(), nTel, token);
				if(etat.getCode()==0){
					//code OK
					clt.setnTel(nTel);
					clientRepository.save(clt);
					return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label,clt );
				}else{
					return new ResponseObject(EnumMessage.OTP_EXPIRED.code, EnumMessage.OTP_EXPIRED.label,null );
				}
				
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}


	@Override
	public ResponseObject TransfertSoldeBetweenTwoClient(TransfertDto transfertDto) {
				
		logger.info("CustomerServiceImpl : TransfertSoldeBetweenTwoClient : START - INPUT : clientSource : " + transfertDto.getIdClientPartnerSource() + " CLIENT DESTINATION :" + transfertDto.getIdClientPartnerCible() + " amount " + transfertDto.getAmount());
		
		try{
			// get client Source
			ClientPartenaire cltPartnerSource = clientPartnerRepository.findByIdClientPartenaireAndIdPartenaire(transfertDto.getIdClientPartnerSource(), transfertDto.getIdPartenaire());
			ClientPartenaire cltPartnerDestination = clientPartnerRepository.findByIdClientPartenaireAndIdPartenaire(transfertDto.getIdClientPartnerCible(), transfertDto.getIdPartenaire());
			
			
			if(!cltPartnerSource.getIdPartenaire().equals(cltPartnerDestination.getIdPartenaire())){
				// the two client have to be related to the same Partner
				logger.info("CustomerServiceImpl : TransfertSoldeBetweenTwoClient : the two Customer dont Have the same Partner : " + transfertDto.getIdPartenaire() );
				return new ResponseObject(EnumMessage.TRANSFERT_NOT_PERMETTED.code, EnumMessage.TRANSFERT_NOT_PERMETTED.label, "the two Customer dont Have the same Partner : "+ cltPartnerSource );
			}
			
			if(cltPartnerSource.getIdClientPartenaire().equals(cltPartnerDestination.getIdClientPartenaire())){
				logger.info("CustomerServiceImpl : TransfertSoldeBetweenTwoClient : the client Source and destination is the same : " + transfertDto.getIdClientPartnerSource() );
				return new ResponseObject(EnumMessage.SAME_CLIENT_FOR_TRANSFERT.code, EnumMessage.SAME_CLIENT_FOR_TRANSFERT.label,cltPartnerSource );
			}
						
			if(cltPartnerSource.getIsActif()==(short) 0){
				logger.info("CustomerServiceImpl : TransfertSoldeBetweenTwoClient : the client Source is not Actif : " + transfertDto.getIdClientPartnerSource() );
				return new ResponseObject(EnumMessage.CLIENT_EXIST_BLACKLISTED.code, EnumMessage.CLIENT_EXIST_BLACKLISTED.label,cltPartnerSource );
			}
			
			if(cltPartnerDestination.getIsActif()==(short) 0){
				logger.info("CustomerServiceImpl : TransfertSoldeBetweenTwoClient : the client Destination is not Actif : " + transfertDto.getIdClientPartnerCible() );
				return new ResponseObject(EnumMessage.CLIENT_EXIST_BLACKLISTED.code, EnumMessage.CLIENT_EXIST_BLACKLISTED.label,cltPartnerDestination );
			}
			
			if(cltPartnerSource.getSoldePartn() < transfertDto.getAmount()){
				logger.info("CustomerServiceImpl : TransfertSoldeBetweenTwoClient : the client Source dont have enought Solde  : " + cltPartnerSource.getSoldePartn() );
				return new ResponseObject(EnumMessage.TRANSFERT_NOT_PERMETTED.code, EnumMessage.TRANSFERT_NOT_PERMETTED.label, "the client Source dont have enought Solde : "+ cltPartnerSource.getSoldePartn() );
			}
			
			PartenaireBprice partner = iPartenaireBpriceRepository.findByIdPartenaire(cltPartnerSource.getIdPartenaire());

			
			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.CEILING);
			
			cltPartnerSource.setSoldePartn(Double.valueOf(df.format(cltPartnerSource.getSoldePartn() - transfertDto.getAmount()).replace(",", ".")));		
			cltPartnerDestination.setSoldePartn(Double.valueOf(df.format(cltPartnerDestination.getSoldePartn() + transfertDto.getAmount()).replace(",", ".")));
			
			clientPartnerRepository.save(cltPartnerSource);
			clientPartnerRepository.save(cltPartnerDestination);
					
			
			//create mouvement solde pour le client source
			MvtSolde mvtSoldeSource = new MvtSolde();
			mvtSoldeSource.setIdClientPartenaire(transfertDto.getIdClientPartnerSource());
			mvtSoldeSource.setMontant(Float.valueOf(df.format(transfertDto.getAmount()).replace(",", ".")));
			mvtSoldeSource.setSens("-");
			mvtSoldeSource.setSource("Transfert depuis  : " +cltPartnerSource.getNom() +" " +cltPartnerSource.getPrenom() +  " vers "+ cltPartnerDestination.getNom() +" " +cltPartnerDestination.getPrenom() );			
			mvtSoldeSource.setTypeOperation("transfert solde");
			mvtSoldeSource.setDateOperation(new Date());
			mvtSoldeSource.setTrace("Transfert depuis  : " +transfertDto.getIdClientPartnerSource() +  " vers "+ transfertDto.getIdClientPartnerCible());
			mvtSoldeService.CreateMvtSolde(mvtSoldeSource);
			
			//create mouvement solde pour le client destination
			MvtSolde mvtSoldeDestination = new MvtSolde();
			mvtSoldeDestination.setIdClientPartenaire(transfertDto.getIdClientPartnerCible());
			mvtSoldeDestination.setMontant(Float.valueOf(df.format(transfertDto.getAmount()).replace(",", ".")));
			mvtSoldeDestination.setSens("+");
			mvtSoldeDestination.setSource("Réception solde depuis  : " + cltPartnerSource.getNom() +" " +cltPartnerSource.getPrenom() +  " vers "+ cltPartnerDestination.getNom() +" " +cltPartnerDestination.getPrenom());			
			mvtSoldeDestination.setTypeOperation("transfert solde");
			mvtSoldeDestination.setDateOperation(new Date());
			mvtSoldeDestination.setTrace("Réception solde depuis  : " +transfertDto.getIdClientPartnerSource() +  " vers "+ transfertDto.getIdClientPartnerCible());
			mvtSoldeService.CreateMvtSolde(mvtSoldeDestination);
			
			List<MvtSolde> listeMouvement = new ArrayList<MvtSolde>();
			listeMouvement.add(mvtSoldeSource);
			listeMouvement.add(mvtSoldeDestination);
			
			//send notification to destinataire
			
			NotificationDto notificationdto = new NotificationDto();
			notificationdto.setBody("Vous avez reçu le montant de " + df.format(transfertDto.getAmount()).replace(",", ".") + " DT de la part de " + cltPartnerSource.getPrenom() +" " +  cltPartnerSource.getNom() );
			notificationdto.setDateDebut(new Date());
			notificationdto.setDescription("Transfert de solde entre clients du partenaire :  " + partner.getAbbreviation());
			notificationdto.setIdPartenaire(cltPartnerDestination.getIdPartenaire());
			notificationdto.setIsSlider((short) 0);
			notificationdto.setTitre("Transfert de solde entre deux clients "+ partner.getAbbreviation());
			notificationdto.setTypeNotification("Transfert de solde");
			
			NotifClientPartner notif = checkNotificationService.sendSingleNotification(notificationdto, transfertDto.getIdClientPartnerCible());
			
			return new ResponseObject(EnumMessage.TRANSFERT_SUCCESS.code, EnumMessage.TRANSFERT_SUCCESS.label, listeMouvement);
			
	     }catch (Exception e){
	    	 logger.info("CustomerServiceImpl : CreateClientBprice : Exception : " + e.getMessage());
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
     }	
		
	}
	
	@Override
	public ResponseObject loginByPhone(String phone, String password, String idPartenaire) {
		try {
			logger.info("UserServiceImpl : LoginByPhone : START - INPUT : client : phone " + phone + "password :" + password);

			CryptDecyp c = new CryptDecyp();
			String pwdCrypt = c.encrypt(password);
		
			ResponseObject response = findCustomerByTelephone(phone, idPartenaire);
			if(response.getResult() == 1){
				logger.info("UserServiceImpl : LoginByPhone :  client exist :  " + response.getObjectResponse().toString());

				ClientResponseDto clientResponseDto = (ClientResponseDto)response.getObjectResponse();
				if(clientResponseDto.getPassword().equalsIgnoreCase(pwdCrypt) && clientResponseDto.getIdPartenaire().equals(idPartenaire)){
					logger.info("UserServiceImpl : LoginByPhone :  client connexion OK :  " + clientResponseDto.getIdClient());
					return new ResponseObject(EnumMessage.LOGIN_SUCESS.code, EnumMessage.LOGIN_SUCESS.label,clientResponseDto);
				}else{
					logger.info("UserServiceImpl : LoginByPhone :  client credentials not OK :  " + clientResponseDto.getIdClient());
					return new ResponseObject(EnumMessage.LOGIN_FAILURE.code, EnumMessage.LOGIN_FAILURE.label,null);
				}
			}else{
				logger.info("UserServiceImpl : LoginByPhone :  clientPartner not existant ");

				return new ResponseObject(EnumMessage.LOGIN_FAILURE.code, EnumMessage.LOGIN_FAILURE.label,null);
			}
				
		} catch (Exception e) {
			return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	
	@Override
	public ResponseObject loginByFidCard(String qrCode, String idPartenaire) {
		try {
			logger.info("UserServiceImpl : LoginByFidCard : START - INPUT : client : phone " + qrCode);

					
			ResponseObject response = findClientByQrCodePartnerAndIdPartner(qrCode, idPartenaire);
			if(response.getResult() == 1){
				logger.info("UserServiceImpl : loginByFidCard :  client exist with Qrcode :  " + response.getObjectResponse().toString());
				ClientPartenaire clientPartenaire = (ClientPartenaire)response.getObjectResponse();
				
					logger.info("UserServiceImpl : loginByFidCard :  client existe  but not yet connected :  OTP is required : " + clientPartenaire.getIdClientPartenaire());
					// get ClientBprice to get PhoneNumbre
					Client cltBprice = clientRepository.findByIdClient(clientPartenaire.getIdClient());
					logger.info("UserServiceImpl : loginByFidCard :  send OTP to  :  " + cltBprice.getnTel());
					OneTimePwd otp = checkOtpService.generateOtp(clientPartenaire.getIdPartenaire(), cltBprice.getnTel());
					return new ResponseObject(EnumMessage.LOGIN_NOT_YET_CONNECTED.code, EnumMessage.LOGIN_NOT_YET_CONNECTED.label,otp);
			
			}else{
				logger.info("UserServiceImpl : loginByFidCard :  clientPartner not existant ");
				return new ResponseObject(EnumMessage.LOGIN_FAILURE.code, EnumMessage.LOGIN_FAILURE.label,null);
			}
				
		} catch (Exception e) {
			return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	
	@Override
	public ResponseObject updateClientPartnerAfterConnexion(ClientPartnerAfterConnexionDto clientPartnerAfterConnexionDto) {
		try{			
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientPartenaire(clientPartnerAfterConnexionDto.getIdClientPartner());
			if(null != cltPartner){
				if(clientPartnerAfterConnexionDto.getIsconnected()==1)
					cltPartner.setIsActif(clientPartnerAfterConnexionDto.getIsconnected());
				if(clientPartnerAfterConnexionDto.getPassword()!= null)
					cltPartner.setPassword(clientPartnerAfterConnexionDto.getPassword());
				if(clientPartnerAfterConnexionDto.getTokenNotification()!= null)
					cltPartner.setTokenNotification(clientPartnerAfterConnexionDto.getTokenNotification());
				
				clientPartnerRepository.save(cltPartner);
				return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, cltPartner);
			}else{
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
			}
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	
	
	@Override
	public ResponseObject resendPassword(String phone, String idPartenaire) {
		try {
			logger.info("UserServiceImpl : resendPassword : START - INPUT : client : phone " + phone + "idPartenaire :" + idPartenaire);
			
			PartenaireBprice partner = iPartenaireBpriceRepository.findByIdPartenaire(idPartenaire);

			ResponseObject response =  findClientByTelephoneForSpecificPartner(phone, idPartenaire);
			if(response.getResult() == 1){
				ClientPartenaire cltPartner  = (ClientPartenaire)response.getObjectResponse();

				logger.info("UserServiceImpl : resendPassword :  client existe OK :  " + cltPartner.getIdClientPartenaire());
				CryptDecyp c = new CryptDecyp();
				String pwdDecrypted = c.decrypt(cltPartner.getPassword());
				
				//send SMS for this client : 
				Etat etat = checkOtpService.sendSms(phone, "Votre password de connexion à l'application : "+partner.getAbbreviation() +" est : " + pwdDecrypted, cltPartner.getIdPartenaire());
				if(etat.getCode() == 0){
					logger.info("UserServiceImpl : resendPassword :  sms envoyé avec succès au  :  " + phone);
					return new ResponseObject(EnumMessage.SMS_SENDED_SUCESS.code, EnumMessage.SMS_SENDED_SUCESS.label,phone);
				}else{
					logger.info("UserServiceImpl : resendPassword :  problème d'envoi de sms  :  " + phone);
					return new ResponseObject(EnumMessage.SMS_SENDED_FAILURE.code, EnumMessage.SMS_SENDED_FAILURE.label,phone);
				}
			}else{
				logger.info("UserServiceImpl : resendPassword :  clientPartner not existant ");
				return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label,null);

			}				
		} catch (Exception e) {
			return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}
	
	
 



	@Override
	public ResponseObject findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire(String idpartenaire, Short isActif, String idgroupe) {
		try{
			if(idpartenaire!=null && isActif!=null && idgroupe!=null){
				List<ClientPartenaire> listeClientPartner= clientPartnerRepository.findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire(idpartenaire, (short) 1,idgroupe);
				if(listeClientPartner.size()>0){
					return new ResponseObject(EnumMessage.LIST_CLIENT_NOT_EMPTY.code, EnumMessage.LIST_CLIENT_NOT_EMPTY.label, listeClientPartner);
				}else{
					return new ResponseObject(EnumMessage.LIST_CLIENT_EMPTY.code, EnumMessage.LIST_CLIENT_EMPTY.label, null);
				}
			}else{
				return new ResponseObject(EnumMessage.PARAMETRES_EMPTY.code, EnumMessage.PARAMETRES_EMPTY.label,null);

			}

		}catch (Exception e){
			logger.info("UserServiceImpl : findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire : Exception : " + e.getMessage());
			return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);

		}
	}

	@Override
	public ResponseObject affecteclienttogroupe(String idclient, String idgroupe) {
		try{
			if(idclient!=null && idgroupe!=null){
				ClientPartenaire clientPartner= clientPartnerRepository.findByIdClientPartenaire(idclient);
				if(clientPartner!=null){
					if(groupeClientPartenaireRepository.existsById(idgroupe)){
						clientPartner.setIdGroupeClientPartenaire(idgroupe);
						ClientPartenaire clientPartnernew= clientPartnerRepository.save(clientPartner);
						logger.info("CustomerServiceImpl : affecteclienttogroupe : nouvelle création de client : " + clientPartnernew.getIdClient());
						return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, clientPartnernew);

					}else{
						logger.info("CustomerServiceImpl : affecteclienttogroupe : GROUPE_NOT_EXIST" );
						return new ResponseObject(EnumMessage.GROUPE_NOT_EXIST.code, EnumMessage.GROUPE_NOT_EXIST.label, null);

					}
				}else{
					return new ResponseObject(EnumMessage.CLIENT_NOT_EXIST.code, EnumMessage.CLIENT_NOT_EXIST.label, null);
				}
			}else{
				return new ResponseObject(EnumMessage.PARAMETRES_EMPTY.code, EnumMessage.PARAMETRES_EMPTY.label,null);

			}

		}catch (Exception e){
			logger.info("CustomerServiceImpl : findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire : Exception : " + e.getMessage());
			return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);

		}
	}
	
public ResponseObject updateMyClientPartnerInformations(ClientPartnerDto clientPartnerDto){
		
		try{			
			ClientPartenaire cltPartner = clientPartnerRepository.findByIdClientPartenaire(clientPartnerDto.getIdClient());
			
			Client clt = clientRepository.findByIdClient(cltPartner.getIdClient());
			if(null != cltPartner){	
				
				if(clientPartnerDto.getNom() != null && !clientPartnerDto.getNom().equals("")){
					cltPartner.setNom(clientPartnerDto.getNom());
				}
				if(clientPartnerDto.getPrenom() != null && !clientPartnerDto.getPrenom().equals("")){
					cltPartner.setPrenom(clientPartnerDto.getPrenom());
				}	
				
				if(clientPartnerDto.getUrlImage() != null && !clientPartnerDto.getUrlImage().equals("")){
					cltPartner.setImage(clientPartnerDto.getUrlImage());
				}
				
				if(clientPartnerDto.getAdress() != null && !clientPartnerDto.getAdress().equals("")){
					cltPartner.setAdress(clientPartnerDto.getAdress());
				}
				
				if(clientPartnerDto.getDateNaissance() != null && !clientPartnerDto.getDateNaissance().equals("")){
					clt.setDateNaissance(clientPartnerDto.getDateNaissance());
				}
				
				if(clientPartnerDto.getMail() != null && !clientPartnerDto.getMail().equals("")){
					clt.setEmail(clientPartnerDto.getMail());
				}
				
				clientRepository.save(clt);
				clientPartnerRepository.save(cltPartner);
				
				
				}		
			return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label,cltPartner);
			
			
		}catch (Exception e){
	    	 return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
		}
	}


}
