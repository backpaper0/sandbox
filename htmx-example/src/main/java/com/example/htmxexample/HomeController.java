package com.example.htmxexample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/message")
    public String getMessage() {
        return "message";
    }

    @GetMapping("/other-page")
    public String showOtherPage() {
        return "other-page";
    }
}
