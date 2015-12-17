package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
