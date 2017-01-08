package com.example;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;

public class CorsStudy {

    public static void main(String[] args) throws Exception {

        PathHandler paths = Handlers.path();

        paths.addExactPath("/hello", getMethodCors(Handlers.redirect("/helloworld")));

        paths.addExactPath("/helloworld", getMethodCors(exchange -> {
            exchange.setStatusCode(StatusCodes.OK);
            exchange.getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, "text/plain; charset=UTF-8");
            exchange.getResponseSender().send("Hello, world!");
        }));

        Undertow.builder()
                .addHttpListener(8080, "0.0.0.0")
                .setHandler(paths)
                .build()
                .start();
    }

    static HttpHandler getMethodCors(HttpHandler next) {
        return exchange -> {
            if (exchange.getRequestHeaders().contains(Headers.ORIGIN)) {
                exchange.getResponseHeaders()
                        .put(HttpString.tryFromString("Access-Control-Allow-Origin"), "*");
            }
            next.handleRequest(exchange);
        };
    }
}
