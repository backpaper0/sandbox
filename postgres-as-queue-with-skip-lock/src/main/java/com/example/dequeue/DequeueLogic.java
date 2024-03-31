package com.example.dequeue;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.AppProperties;

@Component
@Profile("dequeue")
public class DequeueLogic implements InitializingBean {

    @Autowired
    private JdbcClient jdbc;
    @Autowired
    private AppProperties appProperties;

    private String sql;

    @Override
    public void afterPropertiesSet() throws Exception {
        var sb = new StringBuilder();
        sb.append("select id from queue where id > :id and processed = :processed order by id asc limit :limit ");
        sb.append(appProperties.getLockMode().getSql());
        this.sql = sb.toString();
    }

    @Transactional
    public void ready() {
        jdbc.sql("select id from gate where id = :id for share").param("id", 1).query().singleValue();
    }

    @Transactional
    public Long dequeue(Long idCondition) {
        var id = jdbc.sql(sql)
                .param("id", idCondition)
                .param("processed", false)
                .param("limit", 1)
                .query(SingleColumnRowMapper.newInstance(long.class))
                .optional().orElse(null);
        if (id != null) {
            System.out.println(id);

            jdbc.sql("update queue set processed = :processed where id = :id")
                    .param("id", id)
                    .param("processed", true)
                    .update();
        }
        return id;
    }
}
