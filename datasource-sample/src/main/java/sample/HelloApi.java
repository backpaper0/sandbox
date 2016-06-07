package sample;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("hello")
public class HelloApi {

    @Inject
    private DataSource dataSource;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() throws SQLException {
        try (Connection con = dataSource.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select 'hello world'")) {
            rs.next();
            return rs.getString(1);
        }
    }
}
