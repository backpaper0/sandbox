package com.example;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SinkAppTest {

    @Autowired
    private Sink sink;

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void testHandle() {
        final Message<String> message = new GenericMessage<>("{\"name\":\"hoge\"}");
        sink.input().send(message);

        outputCapture.expect(containsString("Received: hoge"));
    }
}