package com.example;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
import com.example.DownloadSupport.DefaultDownloadSupport;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Value("${download.temp-dir}")
    String tempDir;
    @Value("${download.secret-key}")
    String secretKey;
    @Value("${download.expire-seconds}")
    long expireSeconds;

    @Bean
    DownloadSupport downloadSupport() {
        return new DefaultDownloadSupport(tempDir, secretKey, expireSeconds);
    }
}

@Controller
class DownloadController {

    @Autowired
    DownloadSupport downloadSupport;

    @GetMapping("/")
    String home() {
        return "index";
    }

    @PostMapping("/download")
    @ResponseBody
    String prepare() {
        return downloadSupport.createFile(
                out -> out.write(LocalDateTime.now().toString().getBytes()));
    }

    @GetMapping("/download/{name}")
    @ResponseBody
    ResponseEntity<StreamingResponseBody> download(@PathVariable String name) {
        StreamingResponseBody body = out -> downloadSupport.readFile(name, in -> {
            byte[] b = new byte[1024];
            int i;
            while (-1 != (i = in.read(b))) {
                out.write(b, 0, i);
            }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("plain", "text"));
        headers.setContentDispositionFormData("filename",
                UUID.randomUUID().toString() + ".txt",
                StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }

    //    @ExceptionHandler({ DownloadTimeoutException.class, DownloadInvalidException.class })
    //    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //    String handle(Exception e) {
    //        return String.valueOf(e.getMessage());
    //    }
}

@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
    }
}
