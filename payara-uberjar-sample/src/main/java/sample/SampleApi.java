package sample;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.enterprise.context.*;
import javax.inject.*;

@RequestScoped
@Path("")
public class SampleApi {
    @Inject
    private SampleService service;
    @GET
    @Produces("text/plain")
    public String list() {
        return service.list().toString();
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
