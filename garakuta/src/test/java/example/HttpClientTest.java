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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = 8080)
class HttpClientTest {

    @Test
    void test(final MockServerClient client) throws Exception {

        client.when(org.mockserver.model.HttpRequest.request("/example")
                .withHeader("X-Request-Id", "xyz"))
                .respond(org.mockserver.model.HttpResponse.response("Hello, world!"));

        final HttpClient http = HttpClient.newHttpClient();

        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/example"))
                .header("X-Request-Id", "xyz")
                .build();

        final BodyHandler<String> responseBodyHandler = BodyHandlers
                .ofString(Charset.forName("UTF-8"));

        final HttpResponse<String> response = http.send(request, responseBodyHandler);

        assertEquals("Hello, world!", response.body());
    }
}
