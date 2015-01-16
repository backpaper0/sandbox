
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class JdkHttpServerExample {

    public static void main(final String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/hello", new HttpHandler() {

            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, 0);
                try (OutputStream out = exchange.getResponseBody()) {
                    out.write("Hello, world!".getBytes());
                }
            }
        });
        server.start();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new URL("http://localhost:8080/hello").openConnection()
                        .getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while (null != (line = in.readLine())) {
                System.out.println(line);
            }
        }

        server.stop(0);
    }
}
