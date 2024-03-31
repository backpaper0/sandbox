package com.example.dequeue;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.AppProperties;

@Component
@Profile("dequeue")
public class DequeueRunner implements ApplicationRunner {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private DequeueLogic logic;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logic.ready();
        var timeout = TimeUnit.SECONDS.toNanos(appProperties.getTimeout());
        var started = System.nanoTime();
        var id = 0L;
        while ((System.nanoTime() - started) < timeout) {
            var currentId = logic.dequeue(id);
            if (currentId != null) {
                id = currentId;
            }
        }
    }
}
