package com.example.htmxexample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page-transition")
public class PageTransitionController {

    @GetMapping
    public String index() {
        return "page-transition/page1";
    }

    @GetMapping("/2")
    public String nextPage() {
        return "page-transition/page2";
    }
}
