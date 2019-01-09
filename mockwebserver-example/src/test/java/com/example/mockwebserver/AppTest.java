package com.example.mockwebserver;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class AppTest {

    @Test
    public void testPost() throws Exception {

        try (final var server = new MockWebServer()) {
            server.enqueue(new MockResponse().setBody("world"));

            final var app = new App(server.url("/").url().toExternalForm());

            assertEquals("world", app.post("hello"));
            assertEquals(1, server.getRequestCount());
            final RecordedRequest request = server.takeRequest();
            assertEquals("FooBar", request.getHeader("X-Meta-Var"));
            assertEquals("hello", request.getBody().readString(StandardCharsets.UTF_8));
        }
    }
}
