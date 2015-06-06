package hello;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("hello")
public class HelloJaxrsResource {

    @Inject
    private HelloEjb helloEjb;

    @Inject
    private ManagedExecutorService executor;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void get(@Suspended AsyncResponse response) {
        String s1 = "Hello, JAX-RS! ";
        String s2 = helloEjb.get();
        CompletableFuture.supplyAsync(
                () -> Stream.of(s1, s2, helloJpa()).collect(
                        Collectors.joining(System.lineSeparator())), executor)
                .thenAccept(response::resume);

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties props = new Properties();
        jobOperator.start("hello-jbatch", props);
    }

    private String helloJpa() {
        try {
            UserTransaction tx = InitialContext
                    .doLookup("java:comp/UserTransaction");
            tx.begin();
            EntityManagerFactory emf = CDI.current()
                    .select(EntityManagerFactory.class).get();
            EntityManager em = emf.createEntityManager();
            List<HelloJpa> list = em.createQuery("FROM HelloJpa a",
                    HelloJpa.class).getResultList();
            String s = list.isEmpty() ? "(;_;)" : list.get(0).getValue();
            tx.commit();
            em.close();
            return s;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
