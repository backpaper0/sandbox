package sample;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.container.*;
import javax.enterprise.context.*;
import javax.inject.*;
import javax.enterprise.concurrent.*;
import javax.annotation.*;

@RequestScoped
@Path("")
public class SampleApi {
    @Inject
    private SampleService service;
    @Resource
    private ManagedExecutorService executor;
    @GET
    @Produces("text/plain")
    public void list(@Suspended AsyncResponse ar) {
        executor.submit(() -> ar.resume(service.list().toString()));
    }
    @Path("{id}")
    @GET
    @Produces("text/plain")
    public String get(@PathParam("id") Integer id) {
        return service.get(id).map(a -> a.toString()).orElse(null);
    }
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/plain")
    public String create(@FormParam("text") String text) {
        return service.create(text).toString();
    }
}
