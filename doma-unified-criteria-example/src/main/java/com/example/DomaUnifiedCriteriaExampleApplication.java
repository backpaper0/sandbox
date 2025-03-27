package com.example;

import java.util.List;
import java.util.function.Consumer;

import org.seasar.doma.boot.Pageables;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.expression.Expressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootApplication
public class DomaUnifiedCriteriaExampleApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(DomaUnifiedCriteriaExampleApplication.class, args);
    }

    @Autowired
    QueryDsl queryDsl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Pageable pageable = PageRequest.of(2, 4);

        SelectOptions selectOptions = Pageables.toSelectOptions(pageable);
        int offset = (int) SelectOptionsAccessor.getOffset(selectOptions);
        int limit = (int) SelectOptionsAccessor.getLimit(selectOptions);

        Animal_ animals_ = new Animal_();

        Consumer<WhereDeclaration> conditions = q -> q.eq(animals_.species, "コグマ");

        List<Animal> animals = queryDsl.from(animals_)
            .where(conditions)
            .orderBy(q -> q.asc(animals_.id))
            .offset(offset)
            .limit(limit)
            .fetch();

        long total = queryDsl.from(animals_).where(conditions).select(Expressions.count()).fetchOne();

        Page<Animal> page = new PageImpl<>(animals, pageable, total);
        page.forEach(animal -> System.out.println(animal.name()));
        // のりお
        // パッチ
        // パンタ
        // ピッコロ
    }

}
