package experiment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RequestScoped
@Transactional
@Path("")
public class SampleResource {

    @Resource(name = "java:app/jdbc/sample")
    DataSource dataSource;

    @PersistenceContext
    EntityManager em;

    @GET
    public String test() {
        StringWriter s = new StringWriter();
        try (PrintWriter out = new PrintWriter(s)) {
            try {
                p(out, dataSource.getConnection());
                p(out, em.unwrap(Connection.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    private static void p(PrintWriter out, Connection con) throws Exception {
        out.println("Connection: " + con);
        out.println("Connection class name: " + con.getClass().getName());
        out.println("databaseProductName: "
                + con.getMetaData().getDatabaseProductName());
        out.println("autoCommit: " + con.getAutoCommit());
    }
}
