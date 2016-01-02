package app;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("hello")
public class HelloApi {

    @Inject
    private HelloService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get(@DefaultValue("world") @QueryParam("name") String name) {
        return service.sayHello(name);
    }
}
