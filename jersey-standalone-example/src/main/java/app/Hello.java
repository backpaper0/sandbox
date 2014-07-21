package app;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("hello")
public class Hello {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String say(@QueryParam("name") @DefaultValue("world") String name) {
        return String.format("Hello, %s!", name);
    }
}
