package com.adcaisse.compta.EndPoint;

import com.adcaisse.compta.dto.ClientDto;
import com.adcaisse.compta.service.IGroupeClientPartenaireService;
import com.bprice.persistance.model.GroupeClientPartenaire;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/v1")
public class GroupeClientPartenaireEndPoint {

    @Autowired
    private IGroupeClientPartenaireService clientPartenaireService;

    @PostMapping("/CreateGroupeClientPartenaire")
    @ApiOperation(value = "Créer un nouveau groupe client  ", notes = "Retourner Le groupe client créé.\n"
            + "\n<b>result = 1 :</b> objet créé avec succes</b> \n"
            + "\n<b>result =-1 :</b>L'object envoyer est vide\n"
            + "\n<b>result = -3 :</b>Query failed\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object CreateGroupeClientPartenaire(HttpServletRequest request, @RequestBody @Valid GroupeClientPartenaire groupeClientPartenaire) {
        return clientPartenaireService.CreateGroupeClientPartenaire(groupeClientPartenaire);
    }

    @PostMapping("/actifGroupeClientPartenaire/{idgroupe}/{type}")
    @ApiOperation(value = "modifer un groupe client  ", notes = "Retourner Le groupe client modifier.\n"
            +"\n type=1 -> actif ,type=0 ->no actif "
            + "\n<b>result = 1 :</b> objet modifié avec succes</b> \n"
            + "\n<b>result =-2 :</b>un ou plusieurs parametres envoyer sont null\n"
            + "\n<b>result =-1 :</b>Type incorrecte\n"
            + "\n<b>result = -3 :</b>Query failed\n"
            + "\n<b>result = -4 :</b>Le groupe n'existe pas\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object actifGroupeClientPartenaire(HttpServletRequest request, @PathVariable ("idgroupe") String idgroupe,@PathVariable ("type") Short type) {
        return clientPartenaireService.actifGroupeClientPartenaire(idgroupe,type);
    }

    @GetMapping("/findAllByIdPartenaireOrderByDateCreationDesc/{idpartenaire}")
    @ApiOperation(value = "Affichier la liste des groupes par partenaire ", notes = "Retourner la liste des groupes par partenaire.\n"
            + "\n<b>result = 1 :</b> La liste est no vide</b> \n"
            + "\n<b>result =-0 :</b>Id est vide\n"
            + "\n<b>result = -3 :</b>Query failed\n"
            + "\n<b>result = -4 :</b>La liste est  vide\n"
            + "\n<b>result = 401 :</b> TOKEN NOT AUTHORIZED\n"
            + "\n<b>result = 402 :</b> TOKEN MISSING.", authorizations = {
            @Authorization(value = "Bearer") }, response = Object.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = Object.class),
            @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "not found") })
    public Object findAllByIdPartenaireOrderByDateCreationDesc(HttpServletRequest request, @PathVariable ("idpartenaire") String idpartenaire) {
        return clientPartenaireService.findAllByIdPartenaireOrderByDateCreationDesc(idpartenaire);
    }
}
