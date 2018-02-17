package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class MvcConfig {

    @Bean
    public RequestMappingHandlerMapping simpleUrlHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }
}
