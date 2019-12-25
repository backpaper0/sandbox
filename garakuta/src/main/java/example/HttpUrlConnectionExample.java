package example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUrlConnectionExample {

    public static void main(final String[] args) throws Exception {
        new HttpUrlConnectionExample().run();
    }

    void run() throws Exception {
        startServer();

        for (int i = 0; i < 5; i++) {
            TimeUnit.MILLISECONDS.sleep(1000);
            final URL url = new URL("http://localhost:6666");
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(10 * 1000);
            con.setReadTimeout(1000);
            System.out.println(con.getResponseCode());
            System.out.println(con.getResponseMessage());
            try (final InputStream in = (InputStream) con.getContent()) {
                System.out.println(new String(in.readAllBytes(), StandardCharsets.UTF_8));
            }
        }
    }

    void startServer() throws Exception {
        final Thread t = new Thread(this::runServer);
        t.setDaemon(true);
        t.start();
    }

    void runServer() {
        try (ServerSocket server = new ServerSocket()) {
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress(6666));
            while (true) {
                final Socket client = server.accept();
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
            while ((header = in.readLine()).isEmpty() == false) {
                final int index = header.indexOf(':');
                final String key = header.substring(0, index).trim().toLowerCase();
                final String value = header.substring(index + 1).trim();
                headers.put(key, value);
            }
            String entityBody = null;
            final String contentLength = headers.get("content-length");
            if (contentLength != null) {
                final char[] cs = new char[Integer.parseInt(contentLength)];
                in.read(cs);
                entityBody = String.valueOf(cs);
            }
            System.out.println(requestLine);
            System.out.println(headers);
            if (entityBody != null) {
                System.out.println(entityBody);
            }

            final String responseBody = "Hello, world! (" + LocalDateTime.now() + ")";
            final BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
            out.write("HTTP/1.0 200 OK\r\n");
            out.write("Content-Type: text/plain; charset=UTF-8\r\n");
            out.write("Content-Length: " + responseBody.length() + "\r\n");
            out.write("\r\n");
            out.write(responseBody);
            out.flush();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
