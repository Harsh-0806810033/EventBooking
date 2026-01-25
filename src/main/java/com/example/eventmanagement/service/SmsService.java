package com.example.eventmanagement.service;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SmsService {

	private SnsClient snsClient;

    public SmsService(SnsClient snsClient) {
    	this.snsClient=snsClient;
    }

    /**
     * Send SMS if phone is present, otherwise skip.
     */
	/*
	 * public void sendSms(String phone, String message) { if (phone == null ||
	 * phone.isEmpty()) {
	 * System.out.println("No phone number provided, skipping SMS."); return; }
	 * 
	 * try { // Ensure phone is in E.164 format, e.g., "+91XXXXXXXXXX" String
	 * toNumber = phone.startsWith("+") ? phone : "+91" + phone;
	 * 
	 * Message.creator( new PhoneNumber(toNumber), new PhoneNumber(FROM_NUMBER),
	 * message ).create();
	 * 
	 * System.out.println("SMS sent to " + toNumber); } catch (ApiException e) {
	 * System.err.println("Failed to send SMS to " + phone + ": " + e.getMessage());
	 * } }
	 */
    
    public void sendSms(String to, String message) {
        if (to == null || to.isEmpty()) return;


        PublishResponse publishResponse = snsClient.publish(
        	    PublishRequest.builder()
        	        .phoneNumber("+91"+to)
        	        .message(message)
        	        .build()
        	);

        System.out.println(publishResponse);
    }

    /**
     * Send notification (email and/or SMS)
     */
    public void notifyUser(String email, String phone, String message) {
        // send email if present
        if (email != null && !email.isEmpty()) {
            // TODO: integrate your EmailService here
            System.out.println("Email sent to " + email + ": " + message);
        }

        // send SMS if present
        sendSms(phone, message);
    }
}
