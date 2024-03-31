package com.example.gate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("gate")
public class GateRunner implements ApplicationRunner {

    @Autowired
    private JdbcClient jdbc;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        jdbc.sql("select id from gate where id = :id for update")
                .param("id", 1)
                .query()
                .singleValue();
        System.out.println("Press any key...");
        System.in.read();
    }
}
