package com.example;

import java.nio.charset.StandardCharsets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Controller
class DownloadController {

    @GetMapping("/")
    String home() {
        return "index";
    }

    @PostMapping("/download")
    @ResponseBody
    String prepare() {
        return "xyz";
    }

    @GetMapping("/download/{name}")
    @ResponseBody
    ResponseEntity<String> download(@PathVariable String name) {
        System.out.println(name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("plain", "text"));
        headers.setContentDispositionFormData("filename", "hoge.txt", StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .headers(headers)
                .body("Hello, world!");
    }
}

@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
    }
}
