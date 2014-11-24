package echo;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("echo")
public class EchoResource {

    @Inject
    private Echo echo;

    @Inject
    private Logger logger;

    @Context
    private UriInfo uriInfo;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String echo(String in) {

        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "RequestUri: {0}", new Object[] { uriInfo.getRequestUri() });
        }

        return echo.apply(in);
    }
}
