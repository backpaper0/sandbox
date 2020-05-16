package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@ExtendWith(HttpClientTestingExtension.class)
class GettingStartedTest {

	@Test
	void hello(HttpClient httpClient, MockWebServer server) throws Exception {
		server.enqueue(new MockResponse()
				.setHeader("Content-Type", "text/plain")
				.setBody("Hello World"));

		final HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(server.url("/hello").uri())
				.build();
		final HttpResponse<String> response = httpClient
				.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals("text/plain", response.headers().firstValue("Content-Type").get());
		assertEquals("Hello World", response.body());
		assertEquals(1, server.getRequestCount());
		final RecordedRequest recordedRequest = server.takeRequest();
		assertEquals("GET", recordedRequest.getMethod());
		assertEquals("/hello", recordedRequest.getPath());
	}
}
