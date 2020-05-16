package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

@ExtendWith(HttpClientTestingExtension.class)
class JacksonIntegrationTest {

	@Test
	void requestBody(HttpClient httpClient, MockWebServer server) throws Exception {
		server.enqueue(new MockResponse()
				.setStatus("HTTP/1.1 204 No Content"));

		final HttpRequest request = HttpRequest.newBuilder()
				.POST(JacksonBodyPublisher.of(new Message("Hello World")))
				.header("Content-Type", "application/json")
				.uri(server.url("/hello").uri())
				.build();

		final HttpResponse<Void> response = httpClient
				.send(request, BodyHandlers.discarding());

		assertEquals(204, response.statusCode());
		assertEquals(1, server.getRequestCount());
		final RecordedRequest recordedRequest = server.takeRequest();
		assertEquals("POST", recordedRequest.getMethod());
		assertEquals("/hello", recordedRequest.getPath());
		try (Buffer body = recordedRequest.getBody()) {
			assertEquals("{\"message\":\"Hello World\"}", body.readUtf8());
		}
	}

	@Test
	void responseBody(HttpClient httpClient, MockWebServer server) throws Exception {
		server.enqueue(new MockResponse()
				.setStatus("HTTP/1.1 200 OK")
				.setHeader("Content-Type", "application/json")
				.setBody("{\"message\":\"Hello World\"}"));

		final HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(server.url("/hello").uri())
				.build();

		final HttpResponse<Message> response = httpClient
				.send(request, JacksonBodyHandler.of(Message.class));

		assertEquals(200, response.statusCode());
		assertEquals("application/json", response.headers().firstValue("Content-Type").get());
		assertEquals(new Message("Hello World"), response.body());
		assertEquals(1, server.getRequestCount());
		final RecordedRequest recordedRequest = server.takeRequest();
		assertEquals("GET", recordedRequest.getMethod());
		assertEquals("/hello", recordedRequest.getPath());
	}
}
