package app;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class EjbStartup {

	@PostConstruct
	public void startup() {
		Logs.add("SingletonSessionBean");
	}
}
