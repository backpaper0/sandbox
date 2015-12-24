package app;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class CdiStartup2 {

    public void handle(
            @Observes @Initialized(ApplicationScoped.class) Object event) {
        Logs.add("Initialized(ApplicationScoped): " + event);
    }
}
