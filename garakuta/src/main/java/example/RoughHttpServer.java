package example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RoughHttpServer {

	private final Handler handler;

	public RoughHttpServer(final Handler handler) {
		this.handler = handler;
	}

	public void start() {
		final Thread t = new Thread(this::runServer);
		t.setDaemon(true);
		t.start();
	}

	void runServer() {
		try (ServerSocket server = new ServerSocket()) {
			server.setReuseAddress(true);
			server.bind(new InetSocketAddress("0.0.0.0", 8888));
			System.out.println("> Server Started: " + server.getLocalSocketAddress());
			while (true) {
				System.out.println("> Wait...");
				final Socket client = server.accept();
				System.out.println("> Accept: " + client.getRemoteSocketAddress());
				final Thread t = new Thread(() -> handleClient(client));
				t.setDaemon(true);
				t.start();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	void handleClient(final Socket client) {
		try {
			final BufferedReader in = new BufferedReader(
					new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
			final String requestLine = in.readLine();
			final Map<String, String> headers = new HashMap<>();
			String header;
			while ((header = in.readLine()) != null && header.isEmpty() == false) {
				final int index = header.indexOf(':');
				final String key = header.substring(0, index).trim().toLowerCase();
				final String value = header.substring(index + 1).trim();
				headers.put(key, value);
			}
			String entity = null;
			final String contentLength = headers.get("content-length");
			if (contentLength != null) {
				final char[] cs = new char[Integer.parseInt(contentLength)];
				in.read(cs);
				entity = String.valueOf(cs);
			}
			final Request request = new Request(requestLine, headers, entity);
			final Response response = handler.handle(request);

			final BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
			out.write("HTTP/1.1 ");
			out.write(Integer.toString(response.getStatusCode()));
			out.write(" ");
			out.write(response.getStatusMessage());
			out.write("\r\n");
			for (final Entry<String, String> entry : response.getHeaders().entrySet()) {
				if (entry.getKey().toLowerCase().equals("content-length")) {
					continue;
				}
				out.write(entry.getKey());
				out.write(": ");
				out.write(entry.getValue());
				out.write("\r\n");
			}
			out.write("Content-Length: " + response.getEntity().length() + "\r\n");
			out.write("\r\n");
			out.write(response.getEntity());
			out.flush();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static class Request {

		private final String requestLine;
		private final Map<String, String> headers;
		private final String entity;

		public Request(final String requestLine, final Map<String, String> headers,
				final String entity) {
			this.requestLine = requestLine;
			this.headers = headers;
			this.entity = entity;
		}

		public String getRequestLine() {
			return requestLine;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public String getEntity() {
			return entity;
		}
	}

	public static class Response {

		private final int statusCode;
		private final String statusMessage;
		private final Map<String, String> headers;
		private final String entity;

		public Response(final int statusCode, final String statusMessage,
				final Map<String, String> headers,
				final String entity) {
			this.statusCode = statusCode;
			this.statusMessage = statusMessage;
			this.headers = headers;
			this.entity = entity;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getStatusMessage() {
			return statusMessage;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public String getEntity() {
			return entity;
		}
	}

	public interface Handler {

		Response handle(Request request);
	}
}
