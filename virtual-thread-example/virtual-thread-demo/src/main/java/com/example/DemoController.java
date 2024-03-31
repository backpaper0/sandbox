package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private JdbcClient jdbc;

    @GetMapping
    Object getMessage(@RequestParam int id) {
        return jdbc
                .sql("select id, text_content from messages where id = :id")
                .param("id", id)
                .query(DataClassRowMapper.newInstance(Message.class))
                .single();
    }

    public record Message(int id, String textContent) {
    }
}
