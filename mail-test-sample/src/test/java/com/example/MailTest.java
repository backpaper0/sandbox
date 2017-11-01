package com.example;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

public class MailTest {

    private Wiser wiser;

    @Test
    public void test() throws Exception {
        final List<WiserMessage> messages = wiser.getMessages();
        assertThat(messages.size(), is(0));

        final String messageId = new Mail().send();

        assertThat(messages.size(), is(1));

        final WiserMessage message = messages.get(0);
        assertThat(message.getEnvelopeSender(), is("test@example.com"));
        assertThat(message.getEnvelopeReceiver(), is("backpaper0@gmail.com"));

        final MimeMessage msg = message.getMimeMessage();
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
