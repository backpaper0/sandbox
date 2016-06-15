package sample;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("sample")
public class SampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String get() {
        return "hello";
    }
}
