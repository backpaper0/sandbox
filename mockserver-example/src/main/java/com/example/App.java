package com.example;

import static org.mockserver.model.HttpRequest.*;
import static org.mockserver.model.HttpResponse.*;
import static org.mockserver.model.JsonBody.*;
import static org.mockserver.model.MediaType.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;

public class App {

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ClientAndServer clientAndServer = ClientAndServer.startClientAndServer(9080);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			clientAndServer.close();
		}));

		clientAndServer
				.when(request()
						.withMethod("GET")
						.withPath("/hello"))
				.respond(response()
						.withStatusCode(200)
						.withContentType(APPLICATION_JSON)
						.withBody(json(Map.of("message", "Hello, world!")))
						.withDelay(TimeUnit.MILLISECONDS, 500L));

		clientAndServer
				.when(request()
						.withMethod("GET")
						.withPath("/tasks"))
				.respond(response()
						.withStatusCode(200)
						.withContentType(APPLICATION_JSON)
						.withBody(json(
								Map.of("tasks", List.of(
										Map.of("id", 1, "content", "foo"),
										Map.of("id", 2, "content", "bar"),
										Map.of("id", 3, "content", "baz")))))
						.withDelay(TimeUnit.MILLISECONDS, 500L));

		clientAndServer
				.when(request()
						.withMethod("GET")
						.withPath("/tasks/1"))
				.respond(response()
						.withStatusCode(200)
						.withContentType(APPLICATION_JSON)
						.withBody(json(Map.of("id", 1, "content", "foo")))
						.withDelay(TimeUnit.MILLISECONDS, 500L));

		clientAndServer
				.when(request()
						.withMethod("POST")
						.withPath("/tasks")
						.withContentType(APPLICATION_JSON)
						.withBody(json(Map.of("content", "qux"), MatchType.ONLY_MATCHING_FIELDS)))
				.respond(response()
						.withStatusCode(201)
						.withContentType(APPLICATION_JSON)
						.withBody(json(Map.of("id", 4)))
						.withDelay(TimeUnit.MILLISECONDS, 1500L));

		clientAndServer
				.when(request()
						.withMethod("POST")
						.withPath("/tasks")
						.withContentType(APPLICATION_JSON)
						.withBody(json(Map.of("content", "quxxx"), MatchType.ONLY_MATCHING_FIELDS)))
				.respond(response()
						.withStatusCode(400)
						.withContentType(APPLICATION_JSON)
						.withDelay(TimeUnit.MILLISECONDS, 1500L));
	}
}
