package com.adcaisse.compta.Enum;

import java.util.HashMap;
import java.util.Map;

public enum EnumMessage {

	ERREUR_QUERY("Query failed",-3),
	SUCCESS_CREATION("objet créé avec succes",1),
	PARTENAIREBPRICE_EMPTY("PartenaireBprice est vide",0),
	LIST_PARTENAIREBPRICES_EMPTY("List des PartenaireBprice est vide",0),
	SUCCESS_UPDATE("objet modifié avec succes",0),
	PARTENAIREBPRICE_NOT_EXIST("PartenaireBprice n'exist pas",-1),
	SUCCESS_DELETE("objet supprimé avec succes",1),
	ID_EMPTY("Id est vide",0),
	PARTENAIREBPRICE_EXIST("PartenaireBprice exist",1),
	LIST_PARTENAIREBPRICE_NOT_EMPTY("List PartenaireBprice n'est pas vide",1),
	LIST_PARTENAIREBPRICE_EMPTY("List PartenaireBprice est vide",0),
	PARAMETRE_EMPTY("le parametre envoyer est null",-2),
	PARAMETRES_EMPTY("un ou plusieurs parametres envoyer sont null",-2),
	POINTVENTE_EMPTY("PointVente est vide",0),
	POINTVENTE_EXIST("PointVente exist",1),
	LIST_POINTVENTE_NOT_EMPTY("List PointVente n'est pas vide",1),
	LIST_POINTVENTE_EMPTY("List PointVente est vide",0),
	POINTVENTE_NOT_EXIST("PointVente n'exist pas",-1),
	POINTVENTE_NOT_EXIST_FOR_JOURNEE("PointVente n'exist pas",-1),
	USER_EMPTY("Utilisateur est vide",0),
	USER_EMPTY_FOR_SESSION("Utilisateur est vide",-4),
	USER_EXIST("Utilisateur exist",1),
	LIST_USER_NOT_EMPTY("List Utilisateur n'est pas vide",1),
	LIST_USER_EMPTY("List Utilisateur est vide",0),
	USER_NOT_EXIST("Utilisateur n'exist pas",-1),
	USER_NOT_EXIST_FOR_SESSION("Utilisateur n'exist pas",-5),
	TYPEUSER_EMPTY("TypeUtilisateur est vide",0),
	TYPEUSER_NULL_FOR_USER("TypeUtilisateur est vide",-1),
	TYPEUSER_EXIST("TypeUtilisateur exist",1),
	LIST_TYPEUSER_NOT_EMPTY("List TypeUtilisateur n'est pas vide",1),
	LIST_TYPEUSER_EMPTY("List TypeUtilisateur est vide",0),
	TYPEUSER_NOT_EXIST("TypeUtilisateur n'exist pas",-1),
	TYPEUSER_NOT_EXIST_FOR_USER("TypeUtilisateur n'exist pas",-2),
	POINTVENTE_ID_EMPTY("id PointVente est null",-2),
	JOURNEE_EMPTY("journee est vide",0),
	JOURNEE_EMPTY_FORSESSION("journee est vide",-1),
	JOURNEE_NOT_EXIST("journee n'exist pas",-4),
	JOURNEE_NOT_EXIST_FOR_SESSION("journee n'exist pas",-2),
	JOURNEE_EXIST("journee exist",1),
	LIST_JOURNEE_NOT_EMPTY("List Journee n'est pas vide",1),
	LIST_JOURNEE_EMPTY("List Journee est vide",0),
	CAISSE_NOT_EXIST("caisse n'exist pas",-5),
	SESSION_EMPTY("session est vide",0),
	SESSION_NOT_EXIST("session n'exist pas",-6),
	SESSION_EXIST("session exist",1),
	JOURNEE_CLOSED("journee est fermée",-4),
	SESSION_CLOSED("session est fermée",-6),
	SUCCESS_LOGIN("login avec succes",1),
	NOT_AUTHORIZED("utilisateur non autorisé",-4),
	USER_NOT_ACTIF("utilisateur non actif",-7),
	LOGIN_OR_PASSWORD_EMPTY("login ou password sont vide",-6),
	TYPE_CONNECTION_EMPTY("type de connection est null",-5),
	LOGIN_EMPTY("login est null",-4),
	LOGIN_IS_USED("login est utilisée",-5),
	PASSWORD_EMPTY("password est null",-6),
	TYPEUSER_EMPTY_FOR_USER("type utilisateur est null",-7),
	PARTENAIRE_NOT_EXIST_FOR_USER("partenaire n'exist pas",-8),
	PROFIL_NOT_EXIST_FOR_USER("profil n'exist pas",-9),
	IDCAISSE_EMPTY("id caisse est null",-6),
	IDCAISSE_EMPTY2("id caisse est null",-8),
	CAISSE_NOT_EXIST1("caisse n'exist pas",-7),
	DEVISE_EMPTY("Devise est vide",0),
	DEVISE_EMPTY_FORSESSION("Devise est vide",-1),
	DEVISE_NOT_EXIST("Devise n'exist pas",-4),
	DEVISE_NOT_EXIST_FOR_SESSION("Devise n'exist pas",-2),
	DEVISE_EXIST("Devise exist",1),
	LIST_DEVISE_NOT_EMPTY("List Devise n'est pas vide",1),
	LIST_DEVISE_EMPTY("List Devise est vide",0),
	LIST_BILLET_NOT_EMPTY("List Billet n'est pas vide",3),
	LIST_BILLET_EMPTY("List Billet est vide",2),
	TABLE_EMPTY_FORSESSION("table est vide",-1),
	TABLE_NOT_EXIST("table n'exist pas",-4),
	TABLE_NOT_EXIST_FOR_SESSION("table n'exist pas",-2),
	TABLE_EXIST("table exist",1),
	LIST_TABLE_NOT_EMPTY("List table n'est pas vide",1),
	LIST_TABLE_EMPTY("List table est vide",0),
	TABLE_EMPTY("table est vide",0),
	OPERTATIONTYPE_NOT_EXIST("OperationType n'exist pas",-4),
	OPERTATIONTYPE_EXIST("opertationtype exist",1),
	LIST_OPERTATIONTYPE_NOT_EMPTY("List OperationType n'est pas vide",1),
	LIST_OPERTATIONTYPE_EMPTY("List OperationType est vide",0),
	OPERTATIONTYPE_EMPTY("l'OperationType est vide",0),
	IDOPERTATIONTYPE_EMPTY("l'idOperationType est vide",-2),
	DESIGNATION_EMPTY("la designation est vide",-2),
	DESIGNATION_EXIST("la designation exist deja",-1),
	OPERTATION_NOT_EXIST("Operation n'exist pas",-1),
	OPERTATION_EXIST("Operation exist",1),
	LIST_OPERTATION_NOT_EMPTY("List Operation n'est pas vide",1),
	LIST_OPERTATION_EMPTY("List Operation est vide",0),
	OPERTATION_EMPTY("l'Operation est vide",0),
	MATRICULE_EXIST("matricule deja utiliser",-2),
	MATRICULE_EMPTY("matricule est vide",-4),
	VILLE_NOT_EXIST("ville n'exist pas",-5),
	CHART_NOT_EXIST("chart n'exist pas",-6),
	CODE_EXIST("le code est déjà utilisé",-2),
	CLIENT_NOT_EXIST("client n'exist pas",-1),
	CODE_EMPTY("le code est vide",-4),
	CIN_EXIST("cin est  déjà utilisé",-5),
	CIN_EMPTY("cin est vide",-6),
	TEL_EMPTY("N° TEL est vide",-6),
	CLIENT_EXIST("client exist",1),
	LIST_CLIENT_NOT_EMPTY("list client n'est pas vide",1),
	LIST_CLIENT_EMPTY("list client est vide",0),
	CLIENT_EMPLY("le client est vide",-7),
	TYPECAISSE_NOT_EXIST("Type caisse n'exist pas",-4),
	CAISSE_EMPTY("l'object caisse est vide",0),
	CAISSE_EXIST("la caisse exist",1),
	REFERANCE_EXIST("la reference est déja utilisé",-7),
	REFERANCE_EMPTY("reference est null",-8),
	LIST_CAISSE_NOT_EMPTY("la list des caisses est non vide",1),
	LIST_CAISSE_EMPTY("la list des caisses est vide",0),
	LIST_CAISSETYPE_NOT_EMPTY("la list des types caisses  est non vide",1),
	LIST_CAISSETYPE_EMPTY("la list des types caisses est vide",0),
	CAISSETYPE_EXIST("le type caisse exist",1),
	CAISSETYPE_NOT_EXIST("le type caisse n'exist pas",-4),
	CAISSETYPE_EMPTY("le type caisse est vide",-1),
	LIST_DETAILCOMMANDE_EMPTY("List details commande est vide",-4),
	PRODUIT_NOT_EXIST("Le produit n'existe pas",-8),
	STOCK_NOT_DISPO("Stock non disponible",-11),
	COMMAN_IS_CREATED("la commande a été créée avec succés",1),
	OTP_EXPIRED("Code ONE TIME PASSORD NON VALID",-1),
	CLIENT_EXIST_BLACKLISTED("client blacklisté",-2),
	TRANSFERT_NOT_PERMETTED("le transfert n'est pas permis",-3),
	TRANSFERT_SUCCESS("opération de transfert faite avec succès ",1),
	MVT_SOLDE_EXIST("Mouvement existant ",1),
	LOGIN_SUCESS("login success", 1),
	LOGIN_FAILURE("login failure", -1),
	LOGIN_NOT_YET_CONNECTED("Veuillez saisir le code reçu par SMS", 2),
	MVT_SOLDE_NOT_EXIST("Mouvement inexistant ",-1),
	SAME_CLIENT_FOR_TRANSFERT("même client, le transfert n'est pas permis",-2),
	SMS_SENDED_SUCESS("sms envoyé avec succès",1),
	SMS_SENDED_FAILURE("problème d'envoi de sms",0),
	UNKNOWN_TYPE("Type incorrecte",-1),
	EMPTY_OBJECT("L'object envoyer est vide",-1),
	GROUPE_NOT_EXIST("Le groupe n'existe pas",-4),
	LIST_NOT_EMPTY("La liste est no vide",1),
	LIST_EMPTY("La liste est  vide",-4),
	;


	private static final Map<Integer, EnumMessage> BY_CODE = new HashMap<>();
	private static final Map<String, EnumMessage> BY_LABEL = new HashMap<>();

	static {
		for (EnumMessage e : values()) {
			BY_LABEL.put(e.label, e);
			BY_CODE.put(e.code, e);
		}
	}

	public final String label;
	public final Integer code;

	private EnumMessage(String label, Integer code) {
		this.label = label;
		this.code = code;
	}

	public static EnumMessage valueOfLabel(String label) {
		return BY_LABEL.get(label);
	}

	public static EnumMessage valueOfCode(Integer number) {
		return BY_CODE.get(number);
	}

}