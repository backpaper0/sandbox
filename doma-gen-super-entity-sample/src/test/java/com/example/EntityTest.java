package com.example;

import org.junit.*;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.jdbc.dialect.*;
import javax.sql.*;
import org.h2.jdbcx.*;
import com.example.entity.*;

public class EntityTest {
    
    @Test
    public void testEntity() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:sample;INIT=RUNSCRIPT FROM 'sample.sql';DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("secret");
        Dialect dialect = new H2Dialect();
        Config config = new Config() {
            @Override
            public DataSource getDataSource() {
                return dataSource;
            }
            @Override
            public Dialect getDialect() {
                return dialect;
            }
        };
        SampleDao dao = new SampleDaoImpl(config);

        Hoge hoge = new Hoge();
        hoge.foo = 123L;
        dao.insertHoge(hoge);
    
        Fuga fuga = new Fuga();
        fuga.bar = 456L;
        fuga.baz = "HELLO";
        dao.insertFuga(fuga);

        dao.selectAllHoge().forEach(a -> System.out.printf("%1$s - %2$s%n", a.foo, a.createdAt));
        dao.selectAllFuga().forEach(a -> System.out.printf("%1$s, %2$s - %3$s%n", a.bar, a.baz, a.createdAt));
    }
}

