package jersey;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;

import io.undertow.Undertow;

public class JerseyUndertow {

    public static void main(String[] args) {

        ResourceConfig rc = new ResourceConfig().register(HelloApi.class)
                .register(CalcApi.class);

        Undertow undertow = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(new JerseyHandler(rc)).build();
        undertow.start();
    }

    @Path("hello")
    public static class HelloApi {

        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public String say(
                @QueryParam("name") @DefaultValue("world") String name) {
            return String.format("Hello, %s!", name);
        }
    }

    @Path("calc")
    public static class CalcApi {

        @Path("add")
        @POST
        @Produces(MediaType.TEXT_PLAIN)
        public int say(@FormParam("a") int a, @FormParam("b") int b) {
            return a + b;
        }
    }
}
