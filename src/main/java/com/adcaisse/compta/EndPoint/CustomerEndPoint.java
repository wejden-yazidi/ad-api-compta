package com.adcaisse.compta.EndPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcaisse.compta.dto.ClientDto;
import com.adcaisse.compta.dto.ClientDtoForUpdate;
import com.adcaisse.compta.dto.ClientImportedDto;
import com.adcaisse.compta.dto.ClientPartnerAfterConnexionDto;
import com.adcaisse.compta.dto.ClientPartnerConnexionDto;
import com.adcaisse.compta.dto.ClientPartnerDto;
import com.adcaisse.compta.dto.Etat;
import com.adcaisse.compta.dto.InputClientPartnerDto;
import com.adcaisse.compta.dto.TelClientDtoForUpdate;
import com.adcaisse.compta.dto.TransfertDto;
import com.adcaisse.compta.repository.IClientBpriceRepository;
import com.adcaisse.compta.repository.IClientPartnerRepository;
import com.adcaisse.compta.repository.IPartenaireBpriceRepository;
import com.adcaisse.compta.response.ResponseObject;
import com.adcaisse.compta.service.CheckOtpService;
import com.adcaisse.compta.service.ICustomerService;
import com.adcaisse.compta.util.CryptDecyp;
import com.adcaisse.compta.util.QrGenerator;
import com.bprice.persistance.model.Client;
import com.bprice.persistance.model.ClientPartenaire;
import com.bprice.persistance.model.OneTimePwd;
import com.bprice.persistance.model.PartenaireBprice;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * Created by Hassine on 04/02/2020.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/v1")
public class CustomerEndPoint {
	@Autowired
	private ICustomerService customerService ;
	
	@Autowired
    private IClientBpriceRepository clientRepository;
	
    @Autowired
    private CheckOtpService checkOtpService;
    
    @Autowired
    private IClientPartnerRepository clientPartnerRepository;
    
    @Autowired
    private IPartenaireBpriceRepository partenaireBpriceRepository;
    

	
    @PostMapping("/createClientBprice")
    @ApiOperation(value = "Créer un nouveau client Bprice avec son QrCode généré", notes = "Retourner Le client créé.\n"
            + "\n<b>result = 1 :</b> objet créé avec succes</b> \n"
            + "\n<b>result = 0 :</b>l'object Client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object createClientBprice(HttpServletRequest request, @RequestBody @Valid ClientDto clientDto){
        
    	if (clientDto.getnTel() == null || clientDto.getnTel().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify Telephone number ", null);
		}   	
    	
    	if(clientDto.getType()== null || clientDto.getType().equals("PP")){
    	
	    	if (clientDto.getNom() == null || clientDto.getNom().equals("")) {
	    		return new ResponseObject(-1, "Empty Input, please verify nom Or prenom ", null);
			}    	
	    	
	    	if (clientDto.getPrenom() == null || clientDto.getPrenom().equals("")) {
	    		return new ResponseObject(-1, "Empty Input, please verify prenom field ", null);
			}
	    	
	    	
    	}else if(clientDto.getType()!= null && clientDto.getType().equals("PM")){
    		
    		if (clientDto.getRaisonsocial() == null || clientDto.getRaisonsocial().equals("")) {
	    		return new ResponseObject(-1, "Empty Input, please verify raison social ", null);
			}	
	    	
	    	if (clientDto.getMatfiscale() == null || clientDto.getMatfiscale().equals("")){
	    		return new ResponseObject(-1, "Empty Input, please verify Matricule ficale ", null);
			}   	
	    	
	    	if (clientDto.getSiegesocial() == null || clientDto.getSiegesocial().equals("")) {
	    		return new ResponseObject(-1, "Empty Input, please verify siege social field ", null);
			}	
    		
    		
    	}
		    	
		//    	if (clientDto.getEmail() == null || clientDto.getEmail().equals("")){
		//		return new ResponseObject(-1, "Empty Input, please verify Email ", null);
		//	}
			
		//	if (clientDto.getGenre() == null || clientDto.getGenre().equals("")){
		//		return new ResponseObject(-1, "Empty Input, please verify genre ", null);
		//	}
			
		//	if (clientDto.getDateNaissance() == null || clientDto.getDateNaissance().equals("")){
		//		return new ResponseObject(-1, "Empty Input, please verify Date Naissance ", null);
		//	}
    	       
    			
    	Client client = new Client();
    	client.setNom(clientDto.getNom());
    	client.setPrenom(clientDto.getPrenom());
    	client.setDateNaissance(clientDto.getDateNaissance());
    	client.setnTel(clientDto.getnTel());
    	client.setGenre(clientDto.getGenre());
    	client.setSoldeBprice(0.0);
    	client.setEmail(clientDto.getEmail());
    	client.setfActive((short) 1);
    	client.setDateCreation(new Date());
    	client.setType(clientDto.getType());
    	client.setAdress(clientDto.getAdress());
    	//personne morale for dawarji
    	if(clientDto.getType()  != null && clientDto.getType().equals("PM")){
	    	client.setRaisonsocial(clientDto.getRaisonsocial());
	    	client.setSiegesocial(clientDto.getSiegesocial());
	    	client.setMatfiscale(clientDto.getMatfiscale());
    	}
    	
    	return customerService.CreateClientBprice(client);
    }
    
    
    @PostMapping("/createClientPartner")
    @ApiOperation(value = "Créer un nouveau client pour un Partenaire Donnée", notes = "Retourner Le client créé.\n"
            + "\n<b>result = 1 :</b> objet créé avec succes</b> \n"
            + "\n<b>result = 0 :</b>l'object Client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object createClientPartner(HttpServletRequest request, @RequestBody @Valid ClientPartnerDto clientPartnerDto){
        
    	if(clientPartnerDto.getType() ==null || clientPartnerDto.getType().equals("PP")){		
	    	if (clientPartnerDto.getNom() == null || clientPartnerDto.getNom().equals("")
					|| clientPartnerDto.getPrenom() == null || clientPartnerDto.getPrenom().equals("")) {
	    		return new ResponseObject(-1, "Empty Input, please verify nom Or prenom ", null);
			}	
    	}else if(clientPartnerDto.getType() != null && clientPartnerDto.getType().equals("PM")){
    		if (clientPartnerDto.getRaisonsocial() == null || clientPartnerDto.getRaisonsocial().equals("")){
    			return new ResponseObject(-1, "Empty Input, please verify raison social ", null);
    		}
    		
    		if (clientPartnerDto.getMatfiscale() == null || clientPartnerDto.getMatfiscale().equals("")){
    			return new ResponseObject(-1, "Empty Input, please verify matricule fiscale ", null);
    		}
    		
    	}
    	if (clientPartnerDto.getIdPartenaire() == null || clientPartnerDto.getIdPartenaire().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdPartner parameter ", null);
		}
    	
    	if (clientPartnerDto.getIdClient() == null || clientPartnerDto.getIdClient().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdClient parameter ", null);
		}
    	
    	
    	if (clientPartnerDto.getSourceCreation() == null || clientPartnerDto.getSourceCreation().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the source creation parameter ", null);
		}
    	
//    	if (clientPartnerDto.getIdGroupeClient() == null || clientPartnerDto.getIdGroupeClient().equals("")){
//    		return new ResponseObject(-1, "Empty Input, please verify getIdGroupeClientPartenaire parameter ", null);
//		}
    			
    	ClientPartenaire clientPartner = new ClientPartenaire();
    	clientPartner.setNom(clientPartnerDto.getNom());
    	clientPartner.setPrenom(clientPartnerDto.getPrenom());
    	clientPartner.setIdClient(clientPartnerDto.getIdClient());
    	clientPartner.setIdPartenaire(clientPartnerDto.getIdPartenaire());
    	clientPartner.setSoldePartn(0.0);    	
    	clientPartner.setDateCreation(new Date());
    	clientPartner.setIsActif((short) 1);
    	clientPartner.setIdJournee(clientPartnerDto.getIdJournee());
    	clientPartner.setNbrAlimentationFidelite(0);
    	clientPartner.setIdGroupeClientPartenaire(clientPartnerDto.getIdGroupeClient());
    	clientPartner.setRaisonsocial(clientPartnerDto.getRaisonsocial());
    	clientPartner.setMatfiscale(clientPartnerDto.getMatfiscale());
    	clientPartner.setAdress(clientPartnerDto.getAdress());
        if(clientPartnerDto.getSourceCreation().equalsIgnoreCase("caisse")){
        	if (clientPartnerDto.getIdJournee() == null || clientPartnerDto.getIdJournee().equals("")){
        		return new ResponseObject(-1, "Empty Input, please verify the IdJournee parameter ", null);
    		}
        	clientPartner.setIsconnected((short) 0);
        	if(clientPartnerDto.getQrCode() != null && !clientPartnerDto.getQrCode().isEmpty()){
        		clientPartner.setQrCodePartn(clientPartnerDto.getQrCode());
        	}else{
        		clientPartner.setQrCodePartn(QrGenerator.generateQrCode(10));
        	}
        }else{
        	clientPartner.setIsconnected((short) 1);
        	clientPartner.setTokenNotification(clientPartnerDto.getTokenNotification());
        	//generation d'un QR CODE pour les clients de l'application mobile.
        	clientPartner.setQrCodePartn(QrGenerator.generateQrCode(10));
        }
        
        clientPartner.setSrcCreation(clientPartnerDto.getSourceCreation());
        
        if(clientPartnerDto.getOtp() != null){
			CryptDecyp encr;
			try {
				encr = new CryptDecyp();
				clientPartner.setPassword(encr.encrypt(clientPartnerDto.getOtp()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return new ResponseObject(-1, "Exception in CryptDecyp operation ", null);
			}
		}
    	return customerService.CreateClientPartner(clientPartner);
    }
    
   
    
    @PostMapping("/changeStatusClientPartner/{idClientPartner}/{isActif}")
    @ApiOperation(value = "Bloquer / débloquer un client chez le Partenaire, son statut sera activé / désactivé", notes = "Retourner Le client  mis à jour.\n"
            + "\n<b>result = 1 :</b> objet Mis à jour succes</b> \n"
            + "\n<b>result = 0 :</b>l'object Client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object changeStatusClientPartner(HttpServletRequest request,@PathVariable("idClientPartner") String idClientPartner, @PathVariable("isActif") short isActif){

    	if (idClientPartner == null ||idClientPartner.equals("{}")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClientPartner Parameter ", null);
		}
    	
    	if (isActif != 0 && isActif != 1) {
    		return new ResponseObject(-1, "Empty Input, please verify isActif  Parameter must be 0 / 1 ", null);
		}
    	    	
    	return customerService.blockClientPartner(idClientPartner, isActif);
    }
    
    @PostMapping("/blockClientBprice")
    @ApiOperation(value = "Bloquer un client Bprice, son statut est désactivé", notes = "Retourner Le client bloqué et mis à jour.\n"
            + "\n<b>result = 1 :</b> objet Mis à jour succes</b> \n"
            + "\n<b>result = 0 :</b>l'object Client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object blockClientBprice(HttpServletRequest request, @RequestBody @Valid String idClient){

    	if (idClient == null ||idClient.equals("{}")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClient Parameter ", null);
		}	   	
    	return customerService.blockClientBprice(idClient);
    }
    
    @PostMapping("/findByIdClientBprice")
    @ApiOperation(value = "Retourner un client Bprice par son IdClient", notes = "Retourner un client Unique .\n"
            + "\n<b>result = 1 :</b> objet Client retourné avec succes</b> \n"
            + "\n<b>result = 0 :</b>l'object Client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findByIdClientBprice(HttpServletRequest request, @RequestBody @Valid String idClient){

    	if (idClient == null ||idClient.equals("{}")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClient Parameter ", null);
		}	    	
    	return customerService.findByIdClientBprice(idClient);
    }
    
    @PostMapping("/findByIdClientPartner")
    @ApiOperation(value = "Retourner un client partner par son IdClient", notes = "Retourner un client Unique .\n"
            + "\n<b>result = 1 :</b> objet Client retourné avec succes</b> \n"
            + "\n<b>result = 0 :</b>l'object Client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findByIdClientPartner(HttpServletRequest request, @RequestBody @Valid String idClientpartner){

    	if (idClientpartner == null ||idClientpartner.equals("{}")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClient Parameter ", null);
		}	    	
    	return customerService.findByIdClientPartner(idClientpartner);
    }
    
    @PostMapping("/findCustomerByTelephone")
    @ApiOperation(value = "Retourner un client Bprice par son Téléphone et par idPartner", notes = "Retourner un client Unique - passer le prameter ntel et IdPartner .\n"
            + "\n<b>result = 1 :</b> objet Client retourné avec succes</b> \n"
            + "\n<b>result = 0 :</b>l'object Client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findCustomerByTelephone(HttpServletRequest request, @RequestBody @Valid InputClientPartnerDto inputClientPartnerDto){

    	if (inputClientPartnerDto.getnTel() == null ||inputClientPartnerDto.getnTel().equals("{}")) {
    		return new ResponseObject(-1, "Empty Input, please verify numTel Parameter ", null);
		}	    
    	
    	if (inputClientPartnerDto.getIdPartner() == null ||inputClientPartnerDto.getIdPartner().equals("{}")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdPartner Parameter ", null);
		}	  
    	return customerService.findCustomerByTelephone(inputClientPartnerDto.getnTel(),inputClientPartnerDto.getIdPartner() );
    }
    
    @PostMapping("/findClientPartnerByIdClient")
    @ApiOperation(value = "Rechercher un client d'un partenaire donnée par son IdClient", notes = "Passer uniquement les params suivants :  idClient ET  idPartner .\n"
            + "\n<b>result = 1 :</b> objet Client retourné avec succes</b> \n"
            + "\n<b>result = 0 :</b>l'object Client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findClientPartnerByIdClient(HttpServletRequest request, @RequestBody @Valid ClientPartnerDto clientPartnerDto){

    	if (clientPartnerDto.getIdPartenaire() == null || clientPartnerDto.getIdPartenaire().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdPartner parameter ", null);
		}
    	
    	if (clientPartnerDto.getIdClient() == null || clientPartnerDto.getIdClient().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdClient parameter ", null);
		}
    	
    	return customerService.findClientPartnerByIdClient(clientPartnerDto.getIdClient(), clientPartnerDto.getIdPartenaire());
    }
    
    @PostMapping("/findAllClientByIdPartener/{idPartenaire}")
    @ApiOperation(value = "Retourner tous les clients d'un Partenaire donné", notes = "Retourner tous les clients par IdPartner .\n"
            + "\n<b>result = 1 :</b> Liste des  Clients retournée avec succes</b> \n"
            + "\n<b>result = 0 :</b>La liste est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findAllClientByIdPartener(HttpServletRequest request, @PathVariable("idPartenaire") String idPartenaire){

    	if (idPartenaire == null ||idPartenaire.equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify idPartner Parameter ", null);
		}	    	
    	return customerService.findAllClientByIdPartener(idPartenaire);
    }
    
    @GetMapping("/findAllClientBprice")
    @ApiOperation(value = "Afficher le client selon l'IdPartenaire envoyé", notes = "Retourner la Devise selon l'IdPartenaire envoyer\n"
            + "\n<b>result = 1 :</b> list client n'est pas vide</b> \n"
            + "\n<b>result = 0 :</b>list client est vide\n"
            + "\n<b>result = -3 :</b> Query failed\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findAllByIdPartenaireAndFActive(HttpServletRequest request){
        return  customerService.findAllClientBprice();
    }
    
    @GetMapping("/findAllClientBpriceByStatus/{fActif}")
    @ApiOperation(value = "Afficher la liste des clients Bprice  par statut", notes = "Retourner les clients selon le statut envoyé\n"
            + "\n<b>result = 1 :</b> list client n'est pas vide</b> \n"
            + "\n<b>result = 0 :</b>list client est vide\n"
            + "\n<b>result = -1 :</b>un ou plusieurs parametres envoyés sont null\n"
            + "\n<b>result = -3 :</b> Query failed\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findAllClientBpriceByStatus(HttpServletRequest request,@PathVariable("fActif") Short fActif){
        
    	if (fActif == null ||fActif.equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify fActif Parameter ", null);
		}	 
    	return  customerService.findAllClientBpriceByStatus(fActif);
    }
    
    @GetMapping("/findAllActiveClientByPartenaire/{idPartenaire}")
    @ApiOperation(value = "Afficher la liste des clients Atifs d'un partenaire donné", notes = "Retourner les clients Actifs selon le IdPartenaire envoyé\n"
            + "\n<b>result = 1 :</b> list client n'est pas vide</b> \n"
            + "\n<b>result = 0 :</b>list client est vide\n"
            + "\n<b>result = -1 :</b>un ou plusieurs parametres envoyés sont null\n"
            + "\n<b>result = -3 :</b> Query failed\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findAllActiveClientByPartenaire(HttpServletRequest request,@PathVariable("idPartenaire") String idPartenaire){
        
    	if (idPartenaire == null ||idPartenaire.equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify idPartenaire Parameter ", null);
		}	
    	return  customerService.findAllActiveClientByPartenaire(idPartenaire);
    }
    
    @GetMapping("/findAllActiveAndConnectedClientByPartenaire/{idPartenaire}")
    @ApiOperation(value = "Afficher la liste des clients Atifs et connectés d'un partenaire donné", notes = "Retourner les clients Actifs et connectés selon le IdPartenaire envoyé\n"
            + "\n<b>result = 1 :</b> list client n'est pas vide</b> \n"
            + "\n<b>result = 0 :</b>list client est vide\n"
            + "\n<b>result = -1 :</b>un ou plusieurs parametres envoyés sont null\n"
            + "\n<b>result = -3 :</b> Query failed\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findAllActiveClientAndConnectedByPartenaire(HttpServletRequest request,@PathVariable("idPartenaire") String idPartenaire){
        
    	if (idPartenaire == null ||idPartenaire.equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify idPartenaire Parameter ", null);
		}	
    	return  customerService.findAllActiveAndConnectedClientByPartenaire(idPartenaire);
    }
    
    @PostMapping("/searchClientbyIdPartenaire")
    @ApiOperation(value = "Rechercher une liste des clients par Tél ou Nom ou prénom ou mail - pour un partenaire donnée", notes = "Passer uniquement Le paramètre rechercheValue ET  idPartner .\n"
            + "\n<b>result = 1 :</b> list client n'est pas vide</b> \n"
            + "\n<b>result = 0 :</b>list client est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object searchClientbyIdPartenaire(HttpServletRequest request, @RequestBody @Valid InputClientPartnerDto inputClientPartnerDto){

    	if (inputClientPartnerDto.getIdPartner() == null || inputClientPartnerDto.getIdPartner().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdPartner parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getRechercheValue() == null || inputClientPartnerDto.getRechercheValue().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the rechercheValue parameter ", null);
		}
    	
    	return customerService.searchClientbyIdPartenaire(inputClientPartnerDto.getRechercheValue(), inputClientPartnerDto.getIdPartner());
    }
    
    @GetMapping("/findClientByQrCodeBprice/{qrCodeBprice}")
    @ApiOperation(value = "Rechercher un client par son QrCode principal", notes = "Retourner le client selon le paramètre qrCodeBprice envoyé\n"
            + "\n<b>result = 1 :</b> client retourné avec succès</b> \n"
            + "\n<b>result = 0 :</b>client inexistant\n"
            + "\n<b>result = -1 :</b>un ou plusieurs parametres envoyés sont null\n"
            + "\n<b>result = -3 :</b> Query failed\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findClientByQrCodeBprice(HttpServletRequest request,@PathVariable("qrCodeBprice") String qrCodeBprice){
        
    	if (qrCodeBprice == null ||qrCodeBprice.equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify qrCodeBprice Parameter ", null);
		}	
    	return  customerService.findClientByQrCodeBprice(qrCodeBprice);
    }
    
    @GetMapping("/findClientByQrCodePartner/{qrCodePartner}/{idPartenaire}")
    @ApiOperation(value = "Rechercher un client par son QrCode principal et son IdPartenaire", notes = "Retourner le client selon le paramètre qrCodePartner et idPartner envoyé\n"
            + "\n<b>result = 1 :</b> client retourné avec succès</b> \n"
            + "\n<b>result = 0 :</b>client inexistant\n"
            + "\n<b>result = -1 :</b>un ou plusieurs parametres envoyés sont null\n"
            + "\n<b>result = -3 :</b> Query failed\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findClientByQrCodePartner(HttpServletRequest request,@PathVariable("qrCodePartner") String qrCodePartner, @PathVariable("idPartenaire") String idPartenaire){
        
    	if (qrCodePartner == null ||qrCodePartner.equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify qrCodePartner Parameter ", null);
		}	
    	return  customerService.findClientByQrCodePartnerAndIdPartner(qrCodePartner, idPartenaire);
    }
    
    
    @PostMapping("/updateSoldeForClientBprice")
    @ApiOperation(value = "Mise à jour du solde principal du client", notes = "Mettre à jour le solde du client : passer les paramètres suivants :  String idClient, Double amount, String operation .\n"
            + "\n<b>result = 1 :</b> Mise à jour avec succès</b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object updateSoldeForClientBprice(HttpServletRequest request, @RequestBody @Valid InputClientPartnerDto inputClientPartnerDto){

    	if (inputClientPartnerDto.getIdClient() == null || inputClientPartnerDto.getIdClient().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the getIdClient parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getAmount() == null || inputClientPartnerDto.getAmount().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the Amount parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getOperation() == null || inputClientPartnerDto.getOperation().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the Operation parameter ", null);
		}
    	
    	return customerService.updateSoldeForClientBprice(inputClientPartnerDto.getIdClient(), inputClientPartnerDto.getAmount(),inputClientPartnerDto.getOperation() );
    }
    
    
    
    @PostMapping("/updateSoldeForClientPartner")
    @ApiOperation(value = "Mise à jour du solde client chez le partenaire", notes = "Mettre à jour le solde du client chez le partenaire : passer les paramètres suivants :  String idClient, Double amount, String operation .\n"
            + "\n<b>result = 1 :</b> Mise à jour avec succès</b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object updateSoldeForClientPartner(HttpServletRequest request, @RequestBody @Valid InputClientPartnerDto inputClientPartnerDto){

    	if (inputClientPartnerDto.getIdClient() == null || inputClientPartnerDto.getIdClient().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdClient parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getIdPartner() == null || inputClientPartnerDto.getIdPartner().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the idPartner parameter ", null);
		}
    	
    	
    	if (inputClientPartnerDto.getAmount() == null || inputClientPartnerDto.getAmount().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the Amount parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getOperation() == null || inputClientPartnerDto.getOperation().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the Operation parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getTypeOperation() == null || inputClientPartnerDto.getTypeOperation().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the type Operation parameter ", null);
		}
    	
    	return customerService.updateSoldeForClientPartner(inputClientPartnerDto.getIdClient(), inputClientPartnerDto.getAmount(),inputClientPartnerDto.getOperation(), inputClientPartnerDto.getIdPartner(),inputClientPartnerDto.getTypeOperation()  );
    }
    
    
    @PostMapping("/affectQrCodeToClientPartner")
    @ApiOperation(value = "affecter un QrCode à un client du partenaire", notes = "affecter un QrCode à un client : passer les paramètres suivants :  String QrCode, String idClient .\n"
            + "\n<b>result = 1 :</b> Mise à jour avec succès</b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object affectQrCodeToClientPartner(HttpServletRequest request, @RequestBody @Valid InputClientPartnerDto inputClientPartnerDto){

    	if (inputClientPartnerDto.getIdClient() == null || inputClientPartnerDto.getIdClient().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdPartner parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getQrCode() == null || inputClientPartnerDto.getQrCode().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the QrCode parameter ", null);
		}
    	
    	return customerService.affectQrCodeToClientPartner(inputClientPartnerDto.getQrCode(), inputClientPartnerDto.getIdClient());
    }
    
    @PostMapping("/findClientByTelephoneForSpecificPartner")
    @ApiOperation(value = "Rechercher Client par téléphone pour un partenaire donnée", notes = "Recherche Client par téléhone pour un partenaire : passer les paramètres suivants :  String nTel, String idPartner .\n"
            + "\n<b>result = 1 :</b>Client existant et retourné </b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findClientByTelephoneForSpecificPartner(HttpServletRequest request, @RequestBody @Valid InputClientPartnerDto inputClientPartnerDto){

    	if (inputClientPartnerDto.getnTel() == null || inputClientPartnerDto.getnTel().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the nTel parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getIdPartner() == null || inputClientPartnerDto.getIdPartner().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdPartner parameter ", null);
		}
    	
    	return customerService.findClientByTelephoneForSpecificPartner(inputClientPartnerDto.getnTel(), inputClientPartnerDto.getIdPartner());
    }
    
    
    @PostMapping("/findClientByIdJourneeForSpecificPartner")
    @ApiOperation(value = "Rechercher les Clients ajoutés par Journée et par partenaire donnée", notes = "Rechercher les Clients ajoutés par Journée comptable: passer les paramètres suivants : String idPartenaire, String idJournee .\n"
            + "\n<b>result = 1 :</b>Liste Non vide </b> \n"
            + "\n<b>result = 0 :</b>Liste vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findClientByIdJourneeForSpecificPartner(HttpServletRequest request, @RequestBody @Valid InputClientPartnerDto inputClientPartnerDto){

    	if (inputClientPartnerDto.getIdJournee() == null || inputClientPartnerDto.getIdJournee().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdJournee parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getIdPartner() == null || inputClientPartnerDto.getIdPartner().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdPartner parameter ", null);
		}
    	
    	return customerService.findClientByIdJourneeForSpecificPartner( inputClientPartnerDto.getIdPartner(), inputClientPartnerDto.getIdJournee());
    }
    
    //UpdateClientBpriceByAdmin(Client clientBprice);
    
    @PostMapping("/updateClientBpriceByAdmin")
    @ApiOperation(value = "Mise à jour du client par un Admin Bprice", notes = "Mettre à jour Le client indépendament du partenaire : passer l'objet :  nom, Prenom, email, dateNaissance, genre .\n"
            + "\n<b>result = 1 :</b> Mise à jour avec succès</b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object updateClientBpriceByAdmin(HttpServletRequest request, @RequestBody @Valid ClientDtoForUpdate clientDtoForUpdate){

    	if (clientDtoForUpdate == null){
    		return new ResponseObject(-1, "Empty Input, please verify the clientDtoForUpdate parameter ", null);
		}
    	
    	if (clientDtoForUpdate.getIdClient() == null || clientDtoForUpdate.getIdClient().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClient parmeter ", null);
		}	
    	
    	if ((clientDtoForUpdate.getGenre() == null ||clientDtoForUpdate.getGenre().equals(""))
    		&& (clientDtoForUpdate.getNom() == null ||clientDtoForUpdate.getNom().equals(""))
    		&& (clientDtoForUpdate.getPrenom() == null ||clientDtoForUpdate.getPrenom().equals(""))
    		&& (clientDtoForUpdate.getDateNaissance() == null ||clientDtoForUpdate.getDateNaissance().equals(""))
    		&& (clientDtoForUpdate.getEmail() == null ||clientDtoForUpdate.getEmail().equals(""))){
    		return new ResponseObject(-1, "Empty Input, Just One parameter have to be filled to update the customer ", null);
		}
    	
    	Client client = new Client();
    	
    	    	
    	if (clientDtoForUpdate.getNom() == null || clientDtoForUpdate.getNom().equals("")){
    		client.setNom(clientDtoForUpdate.getNom());
		}
    	
    	if (clientDtoForUpdate.getPrenom() == null || clientDtoForUpdate.getPrenom().equals("")){
    		client.setPrenom(clientDtoForUpdate.getPrenom());
		}
    	
    	if (clientDtoForUpdate.getDateNaissance() == null || clientDtoForUpdate.getDateNaissance().equals("")){
    		client.setDateNaissance(clientDtoForUpdate.getDateNaissance());
		}
    		
    	if (clientDtoForUpdate.getEmail() == null || clientDtoForUpdate.getEmail().equals("")){
    		client.setEmail(clientDtoForUpdate.getEmail());
		}
    	
    	if (clientDtoForUpdate.getGenre() == null || clientDtoForUpdate.getGenre().equals("")){
    		client.setGenre(clientDtoForUpdate.getGenre());
		}    	
    	   	
    	return customerService.updateClientBpriceByAdmin(clientDtoForUpdate);
    }
    
    @PostMapping("/updateTelephoneClient")
    @ApiOperation(value = "Mise à jour du Téléphone d'un client Bprice", notes = "Mettre à jour Le téléphone d'un client : passer l'objet :  idClient, nTel, et le token envoyé par SMS .\n"
            + "\n<b>result = 1 :</b> Mise à jour avec succès</b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object updateTelephoneClient(HttpServletRequest request, @RequestBody @Valid TelClientDtoForUpdate telClientDtoForUpdate){

    	if (telClientDtoForUpdate == null){
    		return new ResponseObject(-1, "Empty Input, please verify the telClientDtoForUpdate parameter ", null);
		}
    	
    	if (telClientDtoForUpdate.getIdClientPartner() == null || telClientDtoForUpdate.getIdClientPartner().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClientPartner parmeter ", null);
		}	
    	
    	if (telClientDtoForUpdate.getnTel() == null ||telClientDtoForUpdate.getnTel().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify nTel parmeter ", null);
		}	
    	
    	if (telClientDtoForUpdate.getToken() == null ||telClientDtoForUpdate.getToken().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify token parmeter ", null);
		}	
    	
    	return customerService.UpdateTelephoneCustomer(telClientDtoForUpdate.getIdClientPartner(), telClientDtoForUpdate.getnTel(), telClientDtoForUpdate.getToken());
    }
    
    @PostMapping("/updateClientBpriceByPartner")
    @ApiOperation(value = "Mise à jour d'un client par un partenaire", notes = "Mettre à jour le nom et le prénom d'un client : passer l'objet :ClientPartnerDto et renseigner éventuellement (le nom, prénom) en option, idClient, et id partenaire  .\n"
            + "\n<b>result = 1 :</b> Mise à jour avec succès</b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object updateClientBpriceByPartner(HttpServletRequest request, @RequestBody @Valid ClientPartnerDto clientPartnerDto){

    	if (clientPartnerDto == null){
    		return new ResponseObject(-1, "Empty Input, please verify the clientPartnerDto parameter ", null);
		}
    	
    	if (clientPartnerDto.getIdClient() == null || clientPartnerDto.getIdClient().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClient parmeter ", null);
		}
    	
    	if (clientPartnerDto.getIdPartenaire() == null ||clientPartnerDto.getIdPartenaire().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify idPartenaire parmeter ", null);
		}
    	
    	
    	
    	return customerService.updateClientBpriceByPartner(clientPartnerDto);
    }
    
    @PostMapping("/loginByPhone")
    @ApiOperation(value = "authentification par login / pwd dans l'app mobile", notes = "s'autehtifier pour accéder à l'app mobile :ClientPartnerConnexionDto et renseigner : phone, pwd, et idPartner  .\n"
            + "\n<b>result = 1 :</b> Login success</b> \n"
            + "\n<b>result = -1 :</b>Login Failure\n"
            + "\n<b>result =-2 :</b>un paramètre est vide\n"
            + "\n<b>result =-3 :</b>une exception est générée\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object loginByPhone(HttpServletRequest request, @RequestBody @Valid ClientPartnerConnexionDto clientPartnerConnexionDto){

    	if (clientPartnerConnexionDto == null){
    		return new ResponseObject(-2, "Empty Input, please verify the clientPartnerConnexionDto parameter ", null);
		}
    	
    	if (clientPartnerConnexionDto.getIdPartenaire() == null || clientPartnerConnexionDto.getIdPartenaire().equals("")) {
    		return new ResponseObject(-2, "Empty Input, please verify IdPartenaire parmeter ", null);
		}
    	
    	if (clientPartnerConnexionDto.getPhone() == null ||clientPartnerConnexionDto.getPhone().equals("")){
    		return new ResponseObject(-2, "Empty Input, please verify phone parmeter ", null);
		}	
    	
    	if (clientPartnerConnexionDto.getPassword()== null ||clientPartnerConnexionDto.getPassword().equals("")){
    		return new ResponseObject(-2, "Empty Input, please verify password parmeter ", null);
		}
    	
    	return customerService.loginByPhone(clientPartnerConnexionDto.getPhone(), clientPartnerConnexionDto.getPassword(), clientPartnerConnexionDto.getIdPartenaire());
    }
    
    
    
    @PostMapping("/loginByFidCard")
    @ApiOperation(value = "authentification par QrCode =+> card FID ", notes = "s'autehtifier pour accéder à l'app mobile :ClientPartnerConnexionDto et renseigner : qrCode et idPartner  .\n"
            + "\n<b>result = 1 :</b> Login success</b> \n"
            + "\n<b>result = -1 :</b>Login Failure\n"
            + "\n<b>result = 2 :</b> (First Connexion) Veuillez saisir le code reçu par SMS\n"
            + "\n<b>result =-3 :</b>une exception est générée\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object loginByFidCard(HttpServletRequest request, @RequestBody @Valid ClientPartnerConnexionDto clientPartnerConnexionDto){

    	if (clientPartnerConnexionDto == null){
    		return new ResponseObject(-2, "Empty Input, please verify the clientPartnerConnexionDto parameter ", null);
		}
    	
    	if (clientPartnerConnexionDto.getIdPartenaire() == null || clientPartnerConnexionDto.getIdPartenaire().equals("")) {
    		return new ResponseObject(-2, "Empty Input, please verify IdPartenaire parmeter ", null);
		}
    	
    	if (clientPartnerConnexionDto.getQrCode() == null || clientPartnerConnexionDto.getQrCode().equals("")) {
    		return new ResponseObject(-2, "Empty Input, please verify IdPartenaire parmeter ", null);
		}
    	
    	return customerService.loginByFidCard(clientPartnerConnexionDto.getQrCode(),  clientPartnerConnexionDto.getIdPartenaire());
    }
    
    
    @PostMapping("/updateClientPartnerAfterConnexion")
    @ApiOperation(value = "after connexion, we have to update some fields : isconnected, tokenNotification, password", notes = "update at least one of this fields : isconnected, tokenNotification, passworde .\n"
            + "\n<b>result = 1 :</b> Mise à jour effectuée</b> \n"
            + "\n<b>result = -1 :</b>client not existant\n"
            + "\n<b>result =-3 :</b>une exception est générée\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object updateClientPartnerAfterConnexion(HttpServletRequest request, @RequestBody @Valid ClientPartnerAfterConnexionDto clientPartnerAfterConnexionDto){

    	if (clientPartnerAfterConnexionDto == null){
    		return new ResponseObject(-1, "Empty Input, please verify the clientPartnerConnexionDto parameter ", null);
		}
    	
    	if ((clientPartnerAfterConnexionDto.getPassword() == null || clientPartnerAfterConnexionDto.getPassword().equals(""))
    		&& (clientPartnerAfterConnexionDto.getTokenNotification() == null || clientPartnerAfterConnexionDto.getTokenNotification().equals(""))
    		&& (clientPartnerAfterConnexionDto.getPassword() == null || clientPartnerAfterConnexionDto.getPassword().equals(""))
    			) {
    		return new ResponseObject(-1, "Empty Input, you have to pass at least one filed : isconnected, tokenNotification, password", null);
		}
    	  	
    	return customerService.updateClientPartnerAfterConnexion(clientPartnerAfterConnexionDto);
    }
    
    @PostMapping("/TransfertSoldeFromPartnerToClientPartner")
    @ApiOperation(value = "transfert solde from partner to given client", notes = "tansfert solde from given partner to given client  : pass : idpartner, idClientPartnerDestination, Amount .\n"
            + "\n<b>result = 1 :</b>Transfert effectué avec succès</b> \n"
            + "\n<b>result = -1 :</b>client not existant\n"
            + "\n<b>result = -2 :</b>le client cible est Blacklisté\n"
            + "\n<b>result =-3 :</b>une exception est générée\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object TransfertSoldeFromPartnerToClientPartner(HttpServletRequest request, @RequestBody @Valid TransfertDto transfertDto){

    	if (transfertDto == null){
    		return new ResponseObject(-1, "Empty Input, please verify the transfertDto object ", null);
		}
    	
    	if (transfertDto.getIdPartenaire() == null || transfertDto.getIdPartenaire().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdPartenaire parmeter ", null);
		}
    	
    	if (transfertDto.getIdClientPartnerCible() == null || transfertDto.getIdClientPartnerCible().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClientPartnerCible parmeter ", null);
		}
    	
    	
    	if (transfertDto.getAmount() == 0 || transfertDto.getAmount() == 0.0) {
    		return new ResponseObject(-1, "Empty Input, please verify amount must be > 0 ", null);
		}
    	
    	if (transfertDto.getTypeOperation() == null || transfertDto.getTypeOperation().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify type Operation parameter", null);
		}
    	
    	return customerService.TransfertSoldeFromPartnerToClientPartner(transfertDto);
    }
    
    
    @PostMapping("/TransfertSoldeBetweenTwoClient")
    @ApiOperation(value = "transfert solde entre deux clients données", notes = "tansfert solde from given Client to another client  : pass : idpartner, idClientPartnerSource, idClientPartnerDestination, Amount .\n"
            + "\n<b>result = 1 :</b>Transfert effectué avec succès</b> \n"
            + "\n<b>result = -1 :</b>client Source not existant\n"
            + "\n<b>result = -1 :</b>client destination not existant\n"
            + "\n<b>result = -2 :</b>un des deux client est Blacklisté\n"
            + "\n<b>result =-3 :</b>une exception est générée\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object TransfertSoldeBetweenTwoClient(HttpServletRequest request, @RequestBody @Valid TransfertDto transfertDto){

    	if (transfertDto == null){
    		return new ResponseObject(-1, "Empty Input, please verify the transfertDto object ", null);
		}
    	
    	if (transfertDto.getIdPartenaire() == null || transfertDto.getIdPartenaire().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdPartenaire parmeter ", null);
		}
    	
    	if (transfertDto.getIdClientPartnerSource() == null || transfertDto.getIdClientPartnerSource().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdPartenaireSource parmeter ", null);
		}
    	
    	if (transfertDto.getIdClientPartnerCible() == null || transfertDto.getIdClientPartnerCible().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdCClientPartnerCible parmeter ", null);
		}
    	
    	if (transfertDto.getAmount() == 0 || transfertDto.getAmount() == 0.0) {
    		return new ResponseObject(-1, "Empty Input, please verify amount must be > 0 ", null);
		}
    	return customerService.TransfertSoldeBetweenTwoClient(transfertDto);
    }
    
    @PostMapping("/resendPassword")
    @ApiOperation(value = "envoyer le password d'un Client par sms", notes = "envoyer le password d'un client par sms : passer les paramètres suivants :  String nTel, String idPartner .\n"
            + "\n<b>result = 1 :</b>sms envoyé avec succès </b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object resendPassword(HttpServletRequest request, @RequestBody @Valid InputClientPartnerDto inputClientPartnerDto){

    	if (inputClientPartnerDto.getnTel() == null || inputClientPartnerDto.getnTel().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the nTel parameter ", null);
		}
    	
    	if (inputClientPartnerDto.getIdPartner() == null || inputClientPartnerDto.getIdPartner().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify the IdPartner parameter ", null);
		}
    	
    	return customerService.resendPassword(inputClientPartnerDto.getnTel(), inputClientPartnerDto.getIdPartner());
    }

	@PostMapping("/affecteclienttogroupe/{idclient}/{idgroupe}")
	@ApiOperation(value = "affecter un client a un groupe", notes = "affecter un client a un groupe \n"
			+ "\n<b>result = 0 :</b>objet modifié avec succes </b> \n"
			+ "\n<b>result =-1 :</b>client n'exist pas\n"
			+ "\n<b>result =-2 :</b>un ou plusieurs parametres envoyer sont null\n"
			+ "\n<b>result =-4 :</b>Le groupe n'existe pas\n"
			+ "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
			+ "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
			@Authorization(value = "Bearer") }, response = Object.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	public Object affecteclienttogroupe(HttpServletRequest request, @PathVariable ("idclient") String idclient,@PathVariable ("idgroupe") String idgroupe){

		return customerService.affecteclienttogroupe(idclient, idgroupe);
	}

	@GetMapping("/findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire/{idPartenaire}/{idgroupe}")
	@ApiOperation(value = "affecter un client a un groupe", notes = "affecter un client a un groupe \n"
			+ "\n<b>result = 1 :</b>list client n'est pas vide</b> \n"
			+ "\n<b>result =0 :</b>list client est vide\n"
			+ "\n<b>result =-3 :</b>Query failed\n"
			+ "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
			+ "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
			@Authorization(value = "Bearer") }, response = Object.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "not found") })
	public Object findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire(HttpServletRequest request, @PathVariable ("idPartenaire") String idPartenaire,@PathVariable ("idgroupe") String idgroupe){

		return customerService.findAllByIdPartenaireAndIsActifAndIdGroupeClientPartenaire(idPartenaire,(short) 1, idgroupe);
	}
    
   
	@PostMapping("/importClientAdcaisse")
    @ApiOperation(value = "importer les clients depuis une source externe vers la BD ADCAISSE ", notes = "Retourner La liste des clients créé.\n"
            + "\n<b>result = 1 :</b> liste insérée avec succes</b> \n"
            + "\n<b>result = 0 :</b>la liste est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object importClientAdcaisse(HttpServletRequest request, @RequestBody @Valid List<ClientImportedDto> listClientImportedDto){
        
		
		List<ClientImportedDto> listInserted = new ArrayList<ClientImportedDto>();
		if(!listClientImportedDto.isEmpty()){
			PartenaireBprice partner = partenaireBpriceRepository.findByIdPartenaire(listClientImportedDto.get(0).getIdPartenaire());
		if(partner != null){
						
		for(ClientImportedDto clientImportedDto : listClientImportedDto){
			if (!clientImportedDto.getnTel().isEmpty() && !clientImportedDto.getNom().isEmpty() && !clientImportedDto.getPrenom().isEmpty()){
				
				Client client = clientRepository.findByNTel(clientImportedDto.getnTel());
				if(client == null){
					//create client Bprice
					client = new Client();
			    	client.setNom(clientImportedDto.getNom());
			    	client.setPrenom(clientImportedDto.getPrenom());
			    	client.setDateNaissance(clientImportedDto.getDateNaissance());
			    	client.setnTel(clientImportedDto.getnTel());
			    	client.setGenre(clientImportedDto.getGenre());
			    	client.setSoldeBprice(0.0);
			    	client.setEmail(clientImportedDto.getEmail());
			    	client.setfActive((short) 1);
			    	client.setDateCreation(new Date());
			    	client.setType("PP");
			    	client.setAdress(clientImportedDto.getAdress());
			    	client.setQrCodeBprice(QrGenerator.generateQrCode(10));
					client = clientRepository.save(client);
				}	
					if(client != null ){
						ClientPartenaire clientPartner =  clientPartnerRepository.findByIdClientAndIdPartenaire(client.getIdClient(), clientImportedDto.getIdPartenaire());
						//create clientPartner						
						if(clientPartner == null){
						clientPartner = new ClientPartenaire();
				    	clientPartner.setNom(client.getNom());
				    	clientPartner.setPrenom(client.getPrenom());
				    	clientPartner.setIdClient(client.getIdClient());
				    	clientPartner.setIdPartenaire(clientImportedDto.getIdPartenaire());
				    	clientPartner.setSoldePartn(0.0);    	
				    	clientPartner.setDateCreation(new Date());
				    	clientPartner.setIsActif((short) 1);
				    	clientPartner.setNbrAlimentationFidelite(0);
				    	clientPartner.setAdress(clientImportedDto.getAdress());
				    	clientPartner.setIsconnected((short) 0);
				    	clientPartner.setQrCodePartn(client.getQrCodeBprice());
				        clientPartner.setSrcCreation("imported");
				        //send otp to clientPartner
						OneTimePwd otp = null;
						try {
							otp = checkOtpService.generateOtp(clientImportedDto.getIdPartenaire(), client.getnTel());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						CryptDecyp encr;
						try {
							encr = new CryptDecyp();
							clientPartner.setPassword(encr.encrypt(otp.getPassword()));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							return new ResponseObject(-1, "Exception in CryptDecyp operation ", null);
						}
					 clientPartner = clientPartnerRepository.save(clientPartner);
					 
					// send sms with credential and APPs links.
					try {
						String msgLink= "";
						if(partner.getLinkAppAndroid() != null){
							msgLink="- sur Android : " + partner.getLinkAppAndroid();							
						}
						if(partner.getLinkAppiOs() != null){
							msgLink = msgLink + " - sur iOs : " + partner.getLinkAppiOs();
						}
						Etat etat = checkOtpService.sendSms(client.getnTel(), "Télécharger votre nouvelle application "+partner.getAbbreviation() +msgLink + " - votre Login : Num Tél, Votre password est : " + otp.getPassword()  , clientImportedDto.getIdPartenaire());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					listInserted.add(clientImportedDto);
					}
					
					}
		    				
			}	
			}
		}
	
	}
		return new ResponseObject(listInserted.size() > 0 ? 1 :0, "La liste des clients insérés", listInserted.size() > 0 ? listInserted : null);
	}  
	
	
	@PostMapping("/testsendSms")
    @ApiOperation(value = "tetser l'envoi d'un sms à un client ", notes = "test send sms.\n"
            + "\n<b>result = 1 :</b> liste insérée avec succes</b> \n"
            + "\n<b>result = 0 :</b>la liste est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object testSendSm(HttpServletRequest request, @RequestBody @Valid String numTel){
		
		Etat etat = null;
		try {
			etat = checkOtpService.sendSms(numTel, "Télécharger votre nouvelle application "+"suchi" +" sur Android : " + "www.link.android" + "- sur Android : " + "https://bit.ly/37OKD6O" + " votre Login : N°Tel, Votre pasword est : " + "12345 "  , "607ee87833c276020ec58ef4");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return etat;        
		
	}
	
	
	@PostMapping("/updateMyClientPartnerInformations")
    @ApiOperation(value = "Mise à jour des informations d'un client partenaire ", notes = "Mettre à jour le nom et le prénom, adresse, mail et image d'un client : passer l'objet :ClientPartnerDto et renseigner éventuellement (le nom, prénom, adresse, mail, urlImage, date naissance) en option, idClientPartneaire (dans idClient), et id partenaire  .\n"
            + "\n<b>result = 1 :</b> Mise à jour avec succès</b> \n"
            + "\n<b>result = 0 :</b>Client inexistant\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object updateMyClientPartnerInformations(HttpServletRequest request, @RequestBody @Valid ClientPartnerDto clientPartnerDto){

    	if (clientPartnerDto == null){
    		return new ResponseObject(-1, "Empty Input, please verify the clientPartnerDto parameter ", null);
		}
    	
    	if (clientPartnerDto.getIdClient() == null || clientPartnerDto.getIdClient().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClientPartner parmeter ", null);
		}	

    	return customerService.updateMyClientPartnerInformations(clientPartnerDto);
    }
    
}
