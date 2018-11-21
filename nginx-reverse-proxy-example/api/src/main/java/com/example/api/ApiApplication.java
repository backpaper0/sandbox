package com.example.api;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @RequestMapping(path = "/hello", produces = "text/plain")
    public String hello(@RequestHeader final HttpHeaders httpHeaders) {
        final StringWriter sw = new StringWriter();
        try (PrintWriter out = new PrintWriter(sw)) {
            httpHeaders.forEach((key, values) -> {
                out.printf("%s%n", key);
                values.forEach(value -> out.printf("    %s%n", value));
            });
            out.flush();
        }
        return sw.toString();
    }

}
