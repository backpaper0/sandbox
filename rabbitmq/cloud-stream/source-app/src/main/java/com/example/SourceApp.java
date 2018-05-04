package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableBinding(Source.class)
@RestController
public class SourceApp {

    private final Source source;

    public static void main(final String[] args) {
        SpringApplication.run(SourceApp.class, args);
    }

    public SourceApp(final Source source) {
        this.source = source;
    }

    @PostMapping
    public void handle(@RequestParam final String name) {
        final Person person = new Person();
        person.setName(name);
        final Message<?> message = MessageBuilder.withPayload(person).build();
        source.output().send(message);
    }

    public static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
