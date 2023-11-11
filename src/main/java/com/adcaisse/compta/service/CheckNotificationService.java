package com.adcaisse.compta.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.adcaisse.compta.dto.NotificationDto;
import com.adcaisse.compta.dto.NotificationSendDto;
import com.adcaisse.compta.response.ResponseObject;
import com.bprice.persistance.model.NotifClientPartner;
import com.bprice.persistance.model.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



@Service
public class CheckNotificationService {

	@Value("${url.notification.sendSingleNotification}")
	private String sendSingleNotification;
	
	@Value("${url.notification.createNotification}")
	private String createNotification;
	
	
	static Logger logger = LoggerFactory.getLogger(CheckNotificationService.class);

		
	public NotifClientPartner sendSingleNotification(NotificationDto notificationDto, String idClientPartner) throws IOException {
		logger.info("CheckNotificationService sendSingleNotification Start");
		ResponseObject responseObject = null;
		NotifClientPartner notifClientPartner=null;
		try {
			
			//create Notification
			Notification notification = this.createNotification(notificationDto);
			
			
			URL url = new URL(sendSingleNotification);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
						
			con.setDoOutput(true);
			Gson gson = new Gson();
			
			NotificationSendDto notificationSendDto = new NotificationSendDto();
			notificationSendDto.setIdClientPartner(idClientPartner);
			notificationSendDto.setIdNotification(notification.getIdNotification());
			String jsonInputString = gson.toJson(notificationSendDto);

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
				//success creation
				notifClientPartner =  gson.fromJson(gson.toJson(responseObject.getObjectResponse()), NotifClientPartner.class);
			}
			
			//notifClientPartner =  gson.fromJson(gson.toJson(responseObject.getObjectResponse()), NotifClientPartner.class);

		} catch (MalformedURLException e) {
			logger.info("Exception :  " +  e.getMessage() +"  " +  e);
		}

		return notifClientPartner;
	}
	
	public Notification createNotification(NotificationDto notificationDto) throws IOException {
		logger.info("CheckNotificationService createNotification Start");
		ResponseObject responseObject = null;
		Notification notification=null;
		try {
			URL url = new URL(createNotification);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
						
			con.setDoOutput(true);
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
			
			String jsonInputString = gson.toJson(notificationDto);

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
			
			notification =  gson.fromJson(gson.toJson(responseObject.getObjectResponse()), Notification.class);

		} catch (MalformedURLException e) {
			logger.info("Exception :  " +  e.getMessage() +"  " +  e);
		}

		return notification;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
