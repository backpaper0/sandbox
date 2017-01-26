package com.example;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Controller
class DownloadController {

    @Value("${download.temp-dir}")
    String tempDir;

    @GetMapping("/")
    String home() {
        return "index";
    }

    @PostMapping("/download")
    @ResponseBody
    String prepare() {
        Path dir = Paths.get(tempDir);
        try {
            Files.createDirectories(dir);
            Path file = dir.resolve(UUID.randomUUID().toString());
            Files.write(file, Collections.singleton(LocalDateTime.now().toString()));
            return Base64.getUrlEncoder().encodeToString(
                    file.getFileName().toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @GetMapping("/download/{name}")
    @ResponseBody
    ResponseEntity<StreamingResponseBody> download(@PathVariable String name) {
        Path file = Paths.get(tempDir)
                .resolve(new String(Base64.getDecoder().decode(name), StandardCharsets.UTF_8));
        StreamingResponseBody body = out -> Files.copy(file, out);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("plain", "text"));
        headers.setContentDispositionFormData("filename", file.getFileName().toString(),
                StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }
}

@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
    }
}
