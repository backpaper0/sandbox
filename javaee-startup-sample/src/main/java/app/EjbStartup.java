package app;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class EjbStartup {

    @PostConstruct
    public void startup() {
        Logs.add("SingletonSessionBean");
    }
}
