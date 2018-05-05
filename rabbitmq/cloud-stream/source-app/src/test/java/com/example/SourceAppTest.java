package com.example;

import static org.junit.Assert.*;

import java.util.concurrent.BlockingQueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SourceAppTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private MessageCollector messageCollector;
    @Autowired
    private Source source;

    @Test
    public void testHandle() throws Exception {
        final MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("name", "hoge");
        testRestTemplate.postForEntity("/", request, Void.TYPE);

        final BlockingQueue<Message<?>> queue = messageCollector.forChannel(source.output());
        assertEquals(1, queue.size());
        final Message<?> message = queue.remove();
        assertEquals("{\"name\":\"hoge\"}", message.getPayload());
    }
}
