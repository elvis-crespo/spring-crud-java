package com.api_movie.api_movie.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.api_movie.api_movie.dtos.MailBody;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mailBody.to());
        msg.setFrom("example@gmail.com");
        msg.setSubject(mailBody.subject());
        msg.setText(mailBody.text());

        javaMailSender.send(msg);
    }
}
