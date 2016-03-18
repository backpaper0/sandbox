package app;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("")
public class Hello {

    @Context
    HttpServletRequest request;

    @GET
    public String get() {
        request.getSession();
        return "hello";
    }
}
