package example;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import example.RoughHttpServer.Response;

public class ServerTimingExample {

	public static void main(final String[] args) throws Exception {
		final RoughHttpServer httpServer = new RoughHttpServer(request -> {
			System.out.println(request.getRequestLine());
			final Map<String, String> headers = new HashMap<>();
			headers.put("Server-Timing", "app;dur=1.2;desc=Application, db;dur=3.4");
			headers.put("Content-Type", "text/html; charset=UTF-8");
			final String entity = "<h1>Hello, world!</h1>";
			return new Response(200, "OK", headers, entity);
		});
		httpServer.start();
		TimeUnit.HOURS.sleep(1);
	}
}
