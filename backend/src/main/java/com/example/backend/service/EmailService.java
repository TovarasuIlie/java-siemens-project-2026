package com.example.backend.service;

import com.example.backend.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendCreateAccountEmail(User user) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("niculai614@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, user.getEmail());
        message.setSubject("Creare cont " + user.getUsername());

        message.setContent("Contul cu ID " + user.getId() + " a fost creat cu succes!", "text/html; charset=utf-8");

        mailSender.send(message);

    }

    public void sendHtmlVerificationEmail(User user) throws MessagingException, IOException {

    }
}
