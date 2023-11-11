package com.adcaisse.compta.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.adcaisse.compta.dto.Etat;
import com.bprice.persistance.model.OneTimePwd;




@Service
public class CheckOtpService {

	@Value("${url.otp.checkOtp}")
	private  String urlCheckOtp;
	
	@Value("${url.otp.generateOtp}")
	private  String urlGenerateOtp;
	
	@Value("${url.notification.sendsms}")
	private  String urlsendsms;
	
	static Logger logger = LoggerFactory.getLogger(CheckOtpService.class);

	public Etat checkOTP(String idApplication, String destination, String otp) throws IOException {
		logger.info("CheckOtpService checkOTP Start");
		
		Etat etat = null;
		final String uri = urlCheckOtp;
		Map<String, String> params = new HashMap<String, String>();
		params.put("idApplication", idApplication);
		params.put("destination", destination);
		params.put("code", otp);
		 
		RestTemplate restTemplate = new RestTemplate();
		etat = restTemplate.getForObject(uri, Etat.class, params);
		//Gson g = new Gson();	
		//clientPartner =  g.fromJson(g.toJson(responseObject.getObjectResponse()), ClientPartenaire.class);
		return etat;
	}
	
	
	public Etat sendSms(String destination, String bodysms, String idApplication) throws IOException {
		logger.info("CheckOtpService sendSms Start");
		
		Etat etat = new Etat();
		final String uri = urlsendsms;
		Map<String, String> params = new HashMap<String, String>();
		params.put("destination", destination);
		params.put("bodysms", bodysms);
		params.put("idApplication", idApplication);
				 
		RestTemplate restTemplate = new RestTemplate();
		etat = restTemplate.getForObject(uri, Etat.class, params);
		
		return etat;
	}
	
	
	
	public OneTimePwd generateOtp(String idApplication, String destination) throws IOException {
		logger.info("CheckOtpService generateOTP Start");
		
		OneTimePwd otp = null;
		final String uri = urlGenerateOtp;
		Map<String, String> params = new HashMap<String, String>();
		params.put("idApplication", idApplication);
		params.put("destination", destination);
		
		 
		RestTemplate restTemplate = new RestTemplate();
		otp = restTemplate.getForObject(uri, OneTimePwd.class, params);
		//Gson g = new Gson();	
		//clientPartner =  g.fromJson(g.toJson(responseObject.getObjectResponse()), ClientPartenaire.class);
		return otp;
	}
	
	
}
