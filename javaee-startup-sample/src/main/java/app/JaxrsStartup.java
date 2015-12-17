package app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("")
public class JaxrsStartup extends Application {

    public JaxrsStartup() {
        Logs.add("JAX-RS");
    }
}
