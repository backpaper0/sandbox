package sample;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.enterprise.context.*;

@RequestScoped
@Path("")
public class SampleApi {
    @GET
    @Produces("text/plain")
    public String get(@QueryParam("name") @DefaultValue("world") String name) {
        return "Hello, " + name + "!";
    }
}
