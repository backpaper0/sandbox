package com.example;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class JUnit4WireMockTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule();

	@Test
	public void test() throws Exception {
		wireMockRule.stubFor(get("/hello").willReturn(ok().withBody("Hello, world!")));

		int port = wireMockRule.port();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(
				HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/hello")).build(),
				BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertEquals("Hello, world!", response.body());
	}
}