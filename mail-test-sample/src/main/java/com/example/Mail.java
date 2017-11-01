package com.example;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class Mail {

    public String send() throws EmailException {
        final SimpleEmail email = new SimpleEmail();
        email.setHostName("localhost");
        email.setSmtpPort(8025);
        email.setSubject("hello");
        email.setMsg("Hello, world!");
        email.setFrom("test@example.com");
        email.addTo("backpaper0@gmail.com");
        email.setSentDate(Timestamp.valueOf(LocalDateTime.now()));
        final String messageId = email.send();
        return messageId;
    }
}
