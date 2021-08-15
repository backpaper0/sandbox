package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.util.Util;

public class ExceptionHandlingIT {

	private HttpClient httpClient;
	private String baseURI;

	@BeforeEach
	void init() {
		httpClient = HttpClient.newHttpClient();
		baseURI = "http://localhost:8080/demo";
	}

	/**
	 * error-page要素で例外をハンドリングできる。
	 * 
	 * @throws Exception
	 */
	@Test
	void exception1() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(baseURI + "/exception1")).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertAll(
				() -> assertEquals(HttpServletResponse.SC_OK, response.statusCode()),
				() -> assertEquals("Handling: ExceptionHandler1b", Util.getHandlingName(response)),
				() -> assertEquals(1, Util.getHandlingCount(response)));
	}

	/**
	 * error-page要素で例外をハンドリングできる。
	 * ハンドリング先でも例外をスローし続けてもlocation要素で設定したパスへディスパッチされるのは1度だけの様子。
	 * これはServlet APIの仕様に書かれているのだろうか？
	 * 
	 * @throws Exception
	 */
	@Test
	void exception2() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(baseURI + "/exception2")).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertAll(
				() -> assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.statusCode()),
				() -> assertEquals("Handling: ExceptionHandler2", Util.getHandlingName(response)),
				() -> assertEquals(1, Util.getHandlingCount(response)));
	}

	/**
	 * 例外がハンドリングされない場合。
	 * WildFlyだとレスポンスヘッダーが設定されない様子。
	 * 
	 * @throws Exception
	 */
	@Test
	void exception3() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(baseURI + "/exception3")).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertAll(
				() -> assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.statusCode()),
				() -> assertEquals("Ready: ExceptionHandler3", Util.getHandlingName(response)),
				() -> assertEquals(0, Util.getHandlingCount(response)));
	}

	/**
	 * error-page要素でエラーステータスコードをハンドリングできる。
	 * 
	 * @throws Exception
	 */
	@Test
	void status1() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(baseURI + "/status1")).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertAll(
				() -> assertEquals(HttpServletResponse.SC_OK, response.statusCode()),
				() -> assertEquals("Handling: StatusHandler1b", Util.getHandlingName(response)),
				() -> assertEquals(1, Util.getHandlingCount(response)));
	}

	/**
	 * error-page要素でエラーステータスコードをハンドリングできる。
	 * ハンドリング先でもsendErrorし続けてもlocation要素で設定したパスへディスパッチされるのは1度だけの様子。
	 * 
	 * @throws Exception
	 */
	@Test
	void status2() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(baseURI + "/status2")).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertAll(
				() -> assertEquals(502, response.statusCode()),
				() -> assertEquals("Handling: StatusHandler2", Util.getHandlingName(response)),
				() -> assertEquals(1, Util.getHandlingCount(response)));
	}

	/**
	 * エラーステータスコードがハンドリングされない場合。
	 * 
	 * @throws Exception
	 */
	@Test
	void status3() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(baseURI + "/status3")).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertAll(
				() -> assertEquals(503, response.statusCode()),
				() -> assertEquals("Ready: StatusHandler3", Util.getHandlingName(response)),
				() -> assertEquals(0, Util.getHandlingCount(response)));
	}

	/**
	 * 例外ハンドリングをチェーンしようとしたけどハンドリングされるのは1度だけの様子。
	 * 
	 * @throws Exception
	 */
	@Test
	void chain1() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(baseURI + "/chain1")).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertAll(
				() -> assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.statusCode()),
				() -> assertEquals("Handling: ChainHandler1b", Util.getHandlingName(response)),
				() -> assertEquals(1, Util.getHandlingCount(response)));
	}

	/**
	 * ステータスエラーコードのハンドリングをチェーンしようとしたけどハンドリングされるのは1度だけの様子。
	 * 
	 * @throws Exception
	 */
	@Test
	void chain2() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(URI.create(baseURI + "/chain2")).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertAll(
				() -> assertEquals(505, response.statusCode()),
				() -> assertEquals("Handling: ChainHandler2b", Util.getHandlingName(response)),
				() -> assertEquals(1, Util.getHandlingCount(response)));
	}
}
