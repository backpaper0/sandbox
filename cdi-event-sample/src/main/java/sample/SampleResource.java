package sample;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RequestScoped
@Path("")
public class SampleResource {

    @Inject
    private Event<Log> event;

    @GET
    public String get() {

        event.fire(new Log("Sample"));

        return "Hello, world!";
    }
}
