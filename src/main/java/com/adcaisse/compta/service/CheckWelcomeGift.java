package com.adcaisse.compta.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adcaisse.compta.dto.CadeauDto;
import com.adcaisse.compta.dto.NotificationDto;
import com.adcaisse.compta.dto.NotificationSendDto;
import com.adcaisse.compta.response.ResponseObject;
import com.adcaisse.compta.util.QrGenerator;
import com.bprice.persistance.model.Cadeau;
import com.bprice.persistance.model.NotifClientPartner;
import com.bprice.persistance.model.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



@Service
public class CheckWelcomeGift {


	
	@Value("${url.notification.findcadeaubytypeanddateandpartenaire}")
	private String findCadeaubyTypeAndDateandPartenaire;
	
	
	static Logger logger = LoggerFactory.getLogger(CheckWelcomeGift.class);

	
	public Cadeau checkWelcomeGiftService(CadeauDto cadeauDto) throws IOException {
		logger.info("CheckWelcomeGift checkWelcomeGiftService Start");
		ResponseObject responseObject = null;
		Cadeau cadeau=null;
		try {
			URL url = new URL(findCadeaubyTypeAndDateandPartenaire);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
						
			con.setDoOutput(true);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
			
			String jsonInputString = gson.toJson(cadeauDto);

			OutputStream os = con.getOutputStream();
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);

			StringBuilder response = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
			}

			responseObject = gson.fromJson(response.toString(),
					ResponseObject.class);
			if(responseObject.getResult()==1){
				cadeau =  gson.fromJson(gson.toJson(responseObject.getObjectResponse()), Cadeau.class);
			}
			
		} catch (MalformedURLException e) {
			logger.info("Exception :  " +  e.getMessage() +"  " +  e);
		}

		return cadeau;
	}
	
	
	
//	 public static void main(String[] args) 
//	    { 
//		 CadeauDto cadeauDto = new CadeauDto();
//		 cadeauDto.setDate(new Date());
//		 cadeauDto.setIdparteanire("5e9625de03f11c32e81a9292");
//		 cadeauDto.setType("Bienvenue");
//		 
//		 try {
//			Cadeau cadeau = checkWelcomeGiftService(cadeauDto);
//			System.out.println(cadeau.getValeur());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	      
//	    } 

	
	
	
	
	
	
	
	
}
