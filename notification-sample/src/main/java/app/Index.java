package app;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;

@Path("")
public class Index {
    @GET
    public InputStream index() {
        return getClass().getResourceAsStream("index.html");
    }
}
