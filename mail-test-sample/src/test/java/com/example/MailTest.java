package com.example;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.SimpleEmail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.wiser.Wiser;

public class MailTest {

    private Wiser wiser;

    @Test
    public void test() throws Exception {
        assertThat(wiser.getMessages().size(), is(0));

        final SimpleEmail email = new SimpleEmail();
        email.setHostName("localhost");
        email.setSmtpPort(8025);
        email.setSubject("hello");
        email.setMsg("Hello, world!");
        email.setFrom("test@example.com");
        email.addTo("backpaper0@gmail.com");
        email.setSentDate(Timestamp.valueOf(LocalDateTime.now()));
        final String messageId = email.send();

        assertThat(wiser.getMessages().size(), is(1));

        final MimeMessage msg = wiser.getMessages().get(0).getMimeMessage();
        assertThat(msg.getMessageID(), is(messageId));
    }

    @Before
    public void setUp() {
        wiser = new Wiser(8025);
        wiser.start();
    }

    @After
    public void tearDown() {
        if (wiser != null) {
            wiser.stop();
        }
    }
}
