package app;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("print")
public class PrintApi {

    @PersistenceContext
    private EntityManager em;
    @Resource
    private ManagedExecutorService executor;
    @Resource
    private DataSource dataSource;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return Stream.of(em, executor, dataSource)
                .map(a -> a.getClass().getName() + "@"
                        + System.identityHashCode(a))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
