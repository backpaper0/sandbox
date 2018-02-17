package com.example;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

@Configuration
public class MvcConfig {

    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(Collections.singletonMap("/hello", helloController()));
        return mapping;
    }

    @Bean
    public HelloController helloController() {
        return new HelloController();
    }
}
