package com.adcaisse.compta.EndPoint;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adcaisse.compta.dto.MvtSoldeDto;
import com.adcaisse.compta.response.ResponseObject;
import com.adcaisse.compta.service.IMvtSoldeService;
import com.bprice.persistance.model.MvtSolde;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * Created by Hassine on 08/03/2020.
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/v1")
public class MvtSoldeEndPoint {
	@Autowired
	private IMvtSoldeService mvtSoldeService ;
	
    @PostMapping("/createMouvementSolde")
    @ApiOperation(value = "Créer un nouveau mouvement du solde ", notes = "Retourner Le mouvement du solde créé.\n"
            + "\n<b>result = 1 :</b> objet créé avec succes</b> \n"
            + "\n<b>result = 0 :</b>l'object crée est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object createMouvementSolde(HttpServletRequest request, @RequestBody @Valid MvtSoldeDto mvtSoldeDto){
        
    			
    	if (mvtSoldeDto.getIdClientPartenaire() == null || mvtSoldeDto.getIdClientPartenaire().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify IdClientPartenaire parameter ", null);
		}	
    	
    	if (mvtSoldeDto.getMontant() == null || mvtSoldeDto.getMontant().equals("")){
    		return new ResponseObject(-1, "Empty Input, please verify Montant parameter ", null);
		}
    	
    	if (mvtSoldeDto.getSens() == null || mvtSoldeDto.getSens().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify sens parameter : should be '+' OR '-'  ", null);
		}
    	
    	if (mvtSoldeDto.getSource() == null || mvtSoldeDto.getSource().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify Source parameter  ", null);
		}
    	
    	if (mvtSoldeDto.getTypeOperation() == null || mvtSoldeDto.getTypeOperation().equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify type Operation  ", null);
		}
    	
    	MvtSolde mvtSole = new MvtSolde();
    	mvtSole.setIdClientPartenaire(mvtSoldeDto.getIdClientPartenaire());
    	mvtSole.setMontant(mvtSoldeDto.getMontant());
    	mvtSole.setSens(mvtSoldeDto.getSens());
    	mvtSole.setSource(mvtSoldeDto.getSource());
    	mvtSole.setTypeOperation(mvtSoldeDto.getTypeOperation());
    	mvtSole.setDateOperation(new Date());
    	return mvtSoldeService.CreateMvtSolde(mvtSole);
    }
       
    
    @PostMapping("/findAllMvtSoldeByCustomer/{idClientPartner}")
    @ApiOperation(value = "Retourner tous les mouvements du solde d'un client donné", notes = "Retourner tous les mouvements par IdClientPartner .\n"
            + "\n<b>result = 1 :</b> Liste des  mouvements retournée avec succes</b> \n"
            + "\n<b>result = 0 :</b>La liste est vide\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findAllMvtSoldeByCustomer(HttpServletRequest request, @PathVariable("idClientPartner") String idClientPartner){

    	if (idClientPartner == null ||idClientPartner.equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify idClientPartner Parameter ", null);
		}	    	
    	return mvtSoldeService.getAllMvtSoldeByCustomer(idClientPartner);
    }
    
    @PostMapping("/findMvtSoldeById/{idMvtSolde}")
    @ApiOperation(value = "Retourner le mouvements du solde par Id donnée", notes = "Retourner le mouvement par IdMouvement .\n"
            + "\n<b>result = 1 :</b> Le mouvement retourné avec succes</b> \n"
            + "\n<b>result = 0 :</b>Pas de mouvement\n"
            + "\n<b>result =-1 :</b>un paramètre est vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findMvtSoldeById(HttpServletRequest request, @PathVariable("idClientPartner") String idMvtSolde){

    	if (idMvtSolde == null ||idMvtSolde.equals("")) {
    		return new ResponseObject(-1, "Empty Input, please verify idMvtSolde Parameter ", null);
		}	    	
    	return mvtSoldeService.getMvtSoldeByIdMvt(idMvtSolde);
    }
    
    
    
}
