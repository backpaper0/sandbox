package sample;

import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Dependent
@DataSourceDefinition(name = "java:global/jdbc/sample", className = "org.h2.jdbcx.JdbcDataSource", url = "${ds.url}", user = "sa", password = "secret")
public class ResourceProvider {

    @Resource(lookup = "java:global/jdbc/sample")
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager entityManager;

    @Produces
    public DataSource getDataSource() {
        return dataSource;
    }

    @Produces
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
