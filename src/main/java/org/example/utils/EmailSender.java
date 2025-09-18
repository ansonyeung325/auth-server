package org.example.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EmailSender {

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.sender}")
    private String sender;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.app.password}")
    private String appPassword;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.starttls.enable}")
    private String starttlsEnable;

    public void send(String toEmail, String subject, String body) throws MessagingException {

        System.out.println("host: " + host);

        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.app.password", appPassword);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, appPassword);
            }
        });

        // Create and send message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }

}
