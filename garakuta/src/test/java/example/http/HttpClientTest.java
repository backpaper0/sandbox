package example.http;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.net.http.HttpResponse.ResponseInfo;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = 8080)
class HttpClientTest {

    String json = "{\"message\":\"Hello, world!\"}";

    @Test
    void test(final MockServerClient client) throws Exception {

        client.when(org.mockserver.model.HttpRequest.request("/example")
                .withHeader("X-Request-Id", "xyz"))
                .respond(org.mockserver.model.HttpResponse
                        .response("{\"message\":\"Hello, world!\"}"));

        final HttpClient http = HttpClient.newHttpClient();

        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/example"))
                .header("X-Request-Id", "xyz")
                .build();

        final BodyHandler<Model> responseBodyHandler = JsonBodyHandler.get(Model.class);

        final HttpResponse<Model> response = http.send(request, responseBodyHandler);

        assertEquals(new Model("Hello, world!"), response.body());
    }

    public static class JsonBodyHandler<T> implements BodyHandler<T> {

        private static ObjectMapper defaultObjectMapper = new ObjectMapper();
        private final ObjectMapper objectMapper;
        private final Class<T> clazz;

        public JsonBodyHandler(final ObjectMapper om, final Class<T> clazz) {
            this.objectMapper = om;
            this.clazz = clazz;
        }

        public static <T> BodyHandler<T> get(final Class<T> clazz) {
            return new JsonBodyHandler<>(defaultObjectMapper, clazz);
        }

        @Override
        public BodySubscriber<T> apply(final ResponseInfo responseInfo) {
            return BodySubscribers
                    .mapping(BodySubscribers.ofByteArray(), bytes -> {
                        try {
                            return objectMapper.readValue(bytes, clazz);
                        } catch (final IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
    }

    public static class Model {

        private final String message;

        @JsonCreator
        public Model(@JsonProperty("message") final String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public int hashCode() {
            return Objects.hash(message);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Model other = (Model) obj;
            return message.equals(other.message);
        }

        @Override
        public String toString() {
            return "Model [message=" + message + "]";
        }
    }
}
