package com.ftms.ftmsapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void prepareAndSend(String recipient, String subject, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("trueblueteens@gmail.com", "Norweld FTMS");
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(message);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            System.out.println("Error in sending email!");
        }
    }

}
