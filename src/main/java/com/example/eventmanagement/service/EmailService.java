package com.example.eventmanagement.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

	public void sendSeatConfirmation(String to, String eventName, int seatNo) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Seat Confirmation - " + eventName);
        message.setText(
            "Hello,\n\n" +
            "Your seat has been confirmed.\n\n" +
            "Event: " + eventName + "\n" +
            "Seat Number: " + seatNo + "\n\n" +
            "Thank you."
        );

        mailSender.send(message);
    }
}
