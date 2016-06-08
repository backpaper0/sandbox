package sample;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("accounts")
public class AccountApi {

    @Inject
    private DataSource dataSource;

    @Inject
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> list() {
        return em.createQuery("SELECT a FROM Account a", Account.class).getResultList();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Account get(@PathParam("id") Long id) {
        return em.find(Account.class, id);
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void create(@FormParam("name") String name) {
        Account entity = new Account();
        entity.name = name;
        em.persist(entity);
    }

    @Path("meta")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMetaData() throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            DatabaseMetaData md = con.getMetaData();
            return Stream.of(md.getURL(), md.getUserName()).map(Objects::toString)
                    .collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
