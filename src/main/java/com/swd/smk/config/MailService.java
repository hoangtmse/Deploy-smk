package com.swd.smk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String to, String subject, String text, String gmail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(gmail); // cần khớp với spring.mail.username
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
