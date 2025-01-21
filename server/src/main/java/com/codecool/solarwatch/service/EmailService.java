package com.codecool.solarwatch.service;

import com.codecool.solarwatch.controller.AuthController;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
        logger.info("sending email from {}to ->{}", fromEmail, to);
        try {
            MimeMessage message = mailSender.createMimeMessage();
    //        message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
//            message.setFrom(fromEmail);
            message.addRecipient(
                    MimeMessage.RecipientType.TO,
                    new InternetAddress(to)
            );
            logger.info("Email was send from {} to {} with mime way", fromEmail, to);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email" ,e);
        }
    }
}
