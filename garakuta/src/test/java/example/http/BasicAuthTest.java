package example.http;

import static org.junit.jupiter.api.Assertions.*;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = 8080)
class BasicAuthTest {

    @Test
    void test(final MockServerClient client) throws Exception {

        client.when(org.mockserver.model.HttpRequest.request("/example"))
                .respond(req -> {

                    final List<String> authorization = req.getHeader("Authorization");

                    if (authorization == null || authorization.isEmpty()) {
                        //一度は認証なしにリクエストが飛ぶっぽい
                        return new org.mockserver.model.HttpResponse()
                                .withStatusCode(401)
                                .withHeader("WWW-Authenticate", "Basic realm=example");
                    }

                    return org.mockserver.model.HttpResponse
                            .response(authorization.stream()
                                    .collect(Collectors.joining()));
                });

        final HttpClient http = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("example", "secret".toCharArray());
                    }
                })
                .build();

        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/example"))
                .build();

        final HttpResponse<String> response = http.send(request, BodyHandlers.ofString());

        assertEquals("Basic " + Base64.getEncoder().encodeToString("example:secret".getBytes()),
                response.body());
    }
}
