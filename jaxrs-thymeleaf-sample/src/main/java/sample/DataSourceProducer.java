package sample;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

public class DataSourceProducer {

    @Resource(name = "jdbc/__default")
    @Produces
    DataSource dataSource;

}
