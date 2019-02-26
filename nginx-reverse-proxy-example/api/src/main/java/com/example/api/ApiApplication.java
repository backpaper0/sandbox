package com.example.api;

import java.time.Duration;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

public class ApiApplication {

    public static void main(final String[] args) {
        final RouterFunction<ServerResponse> routerFunction = RouterFunctions.route()
                .GET("/hello", ApiApplication::getHello)
                .build();
        final HttpHandler httpHandler = RouterFunctions.toHttpHandler(routerFunction);
        final HttpServer server = HttpServer.create().host("0.0.0.0").port(8080)
                .handle(new ReactorHttpHandlerAdapter(httpHandler));
        server.bindUntilJavaShutdown(Duration.ofMinutes(1), a -> {
        });
    }

    static Mono<ServerResponse> getHello(final ServerRequest request) {
        final HttpHeaders headers = request.headers().asHttpHeaders();
        final String s = headers.entrySet().stream().map(a -> a.getKey() + "=" + a.getValue())
                .collect(Collectors.joining("\n", "", "\n"));
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).syncBody(s);
    }
}
