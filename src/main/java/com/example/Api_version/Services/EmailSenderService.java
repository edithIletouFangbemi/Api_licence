package com.example.Api_version.Services;

import com.example.Api_version.entities.Body;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        //SimpleMailMessage message = new SimpleMailMessage();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        message.setContent(body, "text/html"); // Set content as HTML

        mailSender.send(message);


    }
}
