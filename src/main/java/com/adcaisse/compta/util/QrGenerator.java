package com.adcaisse.compta.util;

public class QrGenerator {

	// function to generate a random QR code  of length n 
    
	public static String generateQrCode(int n) 
    { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
	
  
    public static void main(String[] args) 
    { 
  
        // Get the size n 
        int n = 8; 
  
        // Get and display the alphanumeric string 
        System.out.println(QrGenerator.generateQrCode(n)); 
    } 

	
	

}
