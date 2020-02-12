package example;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

class HttpClientTest {

    @Test
    void test() throws Exception {
        final HttpClient http = HttpClient.newHttpClient();

        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/example"))
                .build();

        final BodyHandler<String> responseBodyHandler = BodyHandlers
                .ofString(Charset.forName("UTF-8"));

        final HttpResponse<String> response = http.send(request, responseBodyHandler);

        assertEquals("Hello, world!", response.body());
    }
}
