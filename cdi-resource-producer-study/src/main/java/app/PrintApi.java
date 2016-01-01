package app;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

@RequestScoped
@Path("print")
public class PrintApi {

    @PersistenceContext
    private EntityManager em;
    @Resource
    private ManagedExecutorService executor;
    @Resource
    private DataSource dataSource;
    @EJB
    private ResourceBean resourceBean;

    @GET
    public String get() {
        return Stream.of(em, executor, dataSource)
                .map(a -> a.getClass().getName() + "@"
                        + System.identityHashCode(a))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @Path("ejb")
    @GET
    public void ejb(@Suspended AsyncResponse response) {
        String threadName1 = Thread.currentThread().getName();
        String text1 = resourceBean.get();
        executor.submit(() -> {
            String threadName2 = Thread.currentThread().getName();
            String text2 = resourceBean.get();
            String s = Stream.of(threadName1, text1, threadName2, text2)
                    .collect(Collectors.joining(System.lineSeparator()));
            response.resume(s);
        });
    }
}
