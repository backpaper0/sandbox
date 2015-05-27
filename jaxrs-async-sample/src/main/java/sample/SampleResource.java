package sample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("sample")
public class SampleResource {

    private static final Logger logger = Logger.getLogger(SampleResource.class
            .getName());

    @Resource(name = "java:comp/DefaultManagedExecutorService")
    private ManagedExecutorService executor;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void handle(@Suspended AsyncResponse response) {
        logger.info(() -> "handle request");

        CompletableFuture.supplyAsync(this::longTimeTask, executor).thenAccept(
                response::resume);
    }

    private String longTimeTask() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info(() -> "long time task");
        return "sample";
    }
}
