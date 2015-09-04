package experiment;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("")
public class ExperimentResource {

    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {

        Stream<Object> wrapper = Stream.of(em);

        Stream<Object> raw = Stream.generate(em::getDelegate).limit(3);

        return Stream.concat(wrapper, raw)
                .map(a -> a.getClass().getName() + "@"
                        + System.identityHashCode(a))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
