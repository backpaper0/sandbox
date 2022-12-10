package app;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("")
public class JaxrsStartup extends Application {

	public JaxrsStartup() {
		Logs.add("JAX-RS");
	}
}
