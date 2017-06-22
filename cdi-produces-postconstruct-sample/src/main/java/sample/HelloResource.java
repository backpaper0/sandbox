package sample;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RequestScoped
@Path("hello")
public class HelloResource {

    @Inject
    private HelloService service;

    @GET
    public String hello() {
        return service.hello();
    }
}
