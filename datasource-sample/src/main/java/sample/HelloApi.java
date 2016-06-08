package sample;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        try (Connection con = dataSource.getConnection()) {
            DatabaseMetaData md = con.getMetaData();
            return Stream.of(md.getURL(), md.getUserName())
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
