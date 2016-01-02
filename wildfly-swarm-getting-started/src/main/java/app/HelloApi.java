package app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("hello")
public class HelloApi {

    @GET
    public String get() {
        return "Hello, world!";
    }
}
