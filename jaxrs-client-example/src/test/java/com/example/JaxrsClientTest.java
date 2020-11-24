package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.*;
import static org.mockserver.model.HttpResponse.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.impl.client.HttpClientBuilder;
import org.glassfish.jersey.apache.connector.ApacheHttpClientBuilderConfigurator;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;

import com.fasterxml.jackson.annotation.JsonProperty;

@ExtendWith(MockServerExtension.class)
public class JaxrsClientTest {

    @Test
    void test(MockServerClient mock) throws Exception {
        mock
                .when(request().withMethod("GET").withPath("/hello"))
                .respond(response()
                        .withStatusCode(200).withContentType(org.mockserver.model.MediaType.APPLICATION_JSON)
                        .withBody("{\"message\":\"Hello, world!\"}"));

        Configuration config = new ClientConfig().register(new ApacheHttpClientBuilderConfiguratorImpl());
        Client client = ClientBuilder
                .newBuilder()
                .withConfig(config)
                .build();
        Response response = client
                .target("http://localhost:" + mock.getPort()).path("/hello")
                .request().get();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());

        HelloMessage helloMessage = response.readEntity(HelloMessage.class);
        assertEquals("Hello, world!", helloMessage.message);
    }

    public static class HelloMessage {

        public final String message;

        public HelloMessage(@JsonProperty("message") String message) {
            this.message = message;
        }
    }

    public static class ApacheHttpClientBuilderConfiguratorImpl implements ApacheHttpClientBuilderConfigurator {

        @Override
        public HttpClientBuilder configure(HttpClientBuilder httpClientBuilder) {
            System.out.println("called");
            return httpClientBuilder;
        }
    }
}
