package com.example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetup;

public class GreenMailTest {

    @Rule
    public GreenMailRule greenMail = new GreenMailRule(new ServerSetup[] {
            new ServerSetup(8025, null, ServerSetup.PROTOCOL_SMTP),
            new ServerSetup(8110, null, ServerSetup.PROTOCOL_POP3)
    });

    @Before
    public void createUsers() {
        greenMail.setUser("foo@example.com", "foo", "secret1");
        greenMail.setUser("bar@example.com", "bar", "secret2");
    }

    @Test
    public void test() throws Exception {

        {
            final Properties props = new Properties();
            props.setProperty("mail.smtp.host", "localhost");
            props.setProperty("mail.smtp.port", "8025");
            final Session session = Session.getInstance(props);
            final MimeMessage msg = new MimeMessage(session);
            msg.setFrom("foo@example.com");
            msg.addRecipient(RecipientType.TO, new InternetAddress("bar@example.com"));
            msg.setSubject("hello");
            msg.setText("Hello, world!");
            msg.setSentDate(new Date());
            Transport.send(msg);
        }

        //        {
        //            final MimeMessage[] messages = greenMail.getReceivedMessages();
        //            for (final MimeMessage message : messages) {
        //                message.writeTo(System.out);
        //            }
        //        }

        {
            final Properties props = new Properties();
            props.setProperty("mail.pop3.host", "localhost");
            props.setProperty("mail.pop3.port", "8110");
            final Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("bar", "secret2");
                }
            });
            final Store store = session.getStore("pop3");
            store.connect();
            final Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            final Message[] messages = folder.getMessages();

            assertThat(messages.length, is(1));
            assertThat(messages[0].getSubject(), is("hello"));

            for (final Message message : messages) {
                message.writeTo(System.out);
                message.setFlag(Flag.DELETED, true);
            }
            folder.close(true);

            folder.open(Folder.READ_WRITE);
            assertThat(folder.getMessageCount(), is(0));
            folder.close(true);

            store.close();
        }
    }
}
