package sample;

import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("api")
@DataSourceDefinition(name = "java:global/jdbc/sample", className = "org.h2.jdbcx.JdbcDataSource", url = "jdbc:h2:mem:sample;DB_CLOSE_DELAY=-1", user = "sa", password = "secret")
public class SampleApp extends Application {
}
