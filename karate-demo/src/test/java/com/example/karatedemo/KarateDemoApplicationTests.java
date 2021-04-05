package com.example.karatedemo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import com.intuit.karate.junit5.Karate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class KarateDemoApplicationTests {

	@LocalServerPort
	private int port;

	@Karate.Test
	Karate contextLoads() {
		return Karate.run("demo").relativeTo(getClass())
				.systemProperty("server.url", "http://localhost:" + port);
	}
}
