package com.example;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

@WireMockTest
public class HelloWireMockTest {

	@Test
	void test(WireMockRuntimeInfo wireMockRuntimeInfo) throws Exception {
		WireMock wireMock = wireMockRuntimeInfo.getWireMock();
		wireMock.register(get("/hello").willReturn(ok().withBody("Hello, world!")));

		int port = wireMockRuntimeInfo.getHttpPort();

		HttpClient client = HttpClient.newHttpClient();

		HttpResponse<String> response = client.send(
				HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/hello")).build(),
				BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		assertEquals("Hello, world!", response.body());
	}
}