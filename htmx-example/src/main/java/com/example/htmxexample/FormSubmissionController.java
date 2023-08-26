package com.example.htmxexample;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/form-submission")
public class FormSubmissionController {

    @GetMapping
    public String index(Form form) {
        return "form-submission/form";
    }

    @PostMapping("/confirm")
    public String confirm(Form form) {
        return "form-submission/confirm";
    }

    @PostMapping("/complete")
    public String complete(Form form) {
        return "redirect:/form-submission/completed";
    }

    @GetMapping("/completed")
    public String completed() {
        return "form-submission/completed";
    }

    @ModelAttribute("items")
    public List<Item> items() {
        return List.of(
                new Item("1", "項目1"),
                new Item("2", "項目2"),
                new Item("3", "項目3"));
    }

    public record Form(
            String text,
            Boolean check,
            String radio,
            String[] checks,
            String select) {
    }

    public record Item(
            String value,
            String text) {
    }
}
