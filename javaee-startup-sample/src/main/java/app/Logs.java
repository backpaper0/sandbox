package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("")
public class Logs {

	private static final ArrayList<String> logs = new ArrayList<>();

	public static synchronized void add(String log) {
		logs.add(LocalDateTime.now() + ": " + log);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public synchronized String getAll() {
		return logs.stream()
				.collect(Collectors.joining(System.lineSeparator()));
	}
}
