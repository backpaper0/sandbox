package sample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.MediaType;

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
