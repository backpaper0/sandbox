package example;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import example.RoughHttpServer.Response;

public class HttpUrlConnectionExample {

    public static void main(final String[] args) throws Exception {
        new HttpUrlConnectionExample().run();
    }

    void run() throws Exception {
        startServer();

        TimeUnit.MILLISECONDS.sleep(500);
        final URL url = new URL("http://localhost:8888/foobar");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(10 * 1000);
        con.setReadTimeout(3 * 1000);
        System.out.println(con.getResponseCode());
        System.out.println(con.getResponseMessage());
        try (final InputStream in = (InputStream) con.getContent()) {
            System.out.println(new String(in.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    void startServer() {
        final RoughHttpServer httpServer = new RoughHttpServer(request -> {
            System.out.println(request.getRequestLine());
            System.out.println(request.getHeaders());
            System.out.println(request.getEntity());
            final Map<String, String> headers = Collections.singletonMap("Content-Type",
                    "text/plain; charset=UTF-8");
            final String entity = "Hello, world! (" + LocalDateTime.now() + ")";
            return new Response(200, "OK", headers, entity);
        });
        httpServer.start();
    }
}