package com.example.mockwebserver;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public final class App {

    private final String url;

    public App(final String url) {
        this.url = url;
    }

    public String post(final String content) throws Exception {

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Meta-Var", "FooBar")
                .POST(BodyPublishers.ofString(content))
                .build();

        final var client = HttpClient.newHttpClient();

        return client.send(request, BodyHandlers.ofString()).body();
    }
}
