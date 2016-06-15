package sample;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
@DataSourceDefinition(name = "java:global/jdbc/sample", className = "org.h2.jdbcx.JdbcDataSource", url = "jdbc:h2:mem:sample;DB_CLOSE_DELAY=-1", user = "sa", password = "secret")
public class ResourceProvider {

    @PersistenceContext
    private EntityManager entityManager;

    @Produces
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
