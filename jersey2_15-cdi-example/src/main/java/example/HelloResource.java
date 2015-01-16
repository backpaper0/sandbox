package example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@RequestScoped
@Path("hello")
public class HelloResource {

    @Inject
    private Hello hello;

    @GET
    public String say(@QueryParam("name") @DefaultValue("world") String name) {
        return hello.say(name);
    }
}
