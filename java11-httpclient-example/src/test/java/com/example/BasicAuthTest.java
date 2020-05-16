package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@ExtendWith(HttpClientTestingExtension.class)
class BasicAuthTest {

	@Test
	void noAuthenticator(HttpClient httpClient, MockWebServer server) throws Exception {
		server.enqueue(new MockResponse()
				.setStatus("HTTP/1.1 401 Unauthorized")
				.setHeader("WWW-Authenticate", "Basic realm=Example"));

		final HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(server.url("/hello").uri())
				.build();
		final HttpResponse<String> response = httpClient
				.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(401, response.statusCode());

		assertEquals(1, server.getRequestCount());

		final RecordedRequest recordedRequest = server.takeRequest();
		assertEquals("GET", recordedRequest.getMethod());
		assertEquals("/hello", recordedRequest.getPath());
		assertNull(recordedRequest.getHeader("Authorization"));
	}

	@Test
	void basicAuth(MockWebServer server) throws Exception {
		server.enqueue(new MockResponse()
				.setStatus("HTTP/1.1 401 Unauthorized")
				.setHeader("WWW-Authenticate", "Basic realm=Example"));

		server.enqueue(new MockResponse()
				.setStatus("HTTP/1.1 204 No Content"));

		final HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(server.url("/hello").uri())
				.build();
		final Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("exampleuser", "secretxxx".toCharArray());
			}
		};
		final HttpClient httpClient = HttpClient.newBuilder()
				.authenticator(authenticator)
				.build();
		final HttpResponse<String> response = httpClient
				.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(204, response.statusCode());

		// Authenticatorを設定していても1度は認証情報なしにリクエストが投げられる。
		// WWW-Authenticateを受け取らないと認証方式がわからないから、ということかなぁ？？？
		assertEquals(2, server.getRequestCount());

		final RecordedRequest recordedRequest1 = server.takeRequest();
		assertEquals("GET", recordedRequest1.getMethod());
		assertEquals("/hello", recordedRequest1.getPath());
		assertNull(recordedRequest1.getHeader("Authorization"));

		final RecordedRequest recordedRequest2 = server.takeRequest();
		assertEquals("GET", recordedRequest2.getMethod());
		assertEquals("/hello", recordedRequest2.getPath());
		assertEquals(
				"Basic " + Base64.getEncoder().encodeToString("exampleuser:secretxxx".getBytes()),
				recordedRequest2.getHeader("Authorization"));
	}
}
