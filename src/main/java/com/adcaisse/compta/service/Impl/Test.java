package com.adcaisse.compta.service.Impl;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.adcaisse.compta.response.ResponseObject;
import com.adcaisse.compta.util.CryptDecyp;
import com.bprice.persistance.model.PartenaireBprice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class Test {

	public static void main(String[] args) throws Exception {
		
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		
		Double doub = 10.32455666;
		String doubString = df.format(doub);
		Double ret = Double.parseDouble(doubString.replace(",", "."));
		
		
//			ObjectMapper mapper = new ObjectMapper();
//			
//			String json = mapper.writeValueAsString(resp.getObjectResponse());	
//			
//						
//			System.out.println(resp.getObjectResponse());
//			Gson gson = new Gson(); 
//			PartenaireBprice pert = gson.fromJson(json, 
//					PartenaireBprice.class); 
		
		System.out.println(ret);

	}
	
	
	public static ResponseObject findPartnerById(String idPartenaire) throws IOException {
		ResponseObject responseObject = null;
		final String uri = "http://localhost:8080/v1/findByIdPartenaire/{idPartner}";

		Map<String, String> params = new HashMap<String, String>();
		params.put("idPartner", idPartenaire);
		 
		RestTemplate restTemplate = new RestTemplate();
		 responseObject = restTemplate.getForObject(uri, ResponseObject.class, params);
		 
		//System.out.println(responseObject);

		return responseObject;
	}

}
