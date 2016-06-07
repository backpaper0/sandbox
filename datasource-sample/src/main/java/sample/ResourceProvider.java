package sample;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

@Dependent
public class ResourceProvider {

    @Resource(lookup = "java:global/jdbc/sample")
    private DataSource dataSource;

    @Produces
    public DataSource getDataSource() {
        return dataSource;
    }
}
