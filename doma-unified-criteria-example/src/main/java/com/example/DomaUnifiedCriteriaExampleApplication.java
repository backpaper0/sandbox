package com.example;

import java.util.List;

import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DomaUnifiedCriteriaExampleApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DomaUnifiedCriteriaExampleApplication.class, args);
    }

    @Autowired
    QueryDsl queryDsl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Animal_ animals_ = new Animal_();
        List<Animal> animals = queryDsl.from(animals_)
            .where(where -> where.eq(animals_.species, "コグマ"))
            .orderBy(orderBy -> orderBy.asc(animals_.id))
            .offset(8)
            .limit(3)
            .fetch();
        for (Animal animal : animals) {
            System.out.println(animal.name());
        }
    }

}
