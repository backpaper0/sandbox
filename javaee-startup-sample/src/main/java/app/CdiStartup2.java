package app;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

/*
 * CDIの@Initialized(ApplicationScoped.class)で起動処理。
 * JSR 346の6.7.3. Application context lifecycleを参照。
 * 
 */
@ApplicationScoped
public class CdiStartup2 {

    public void handle(
            @Observes @Initialized(ApplicationScoped.class) Object event) {
        Logs.add("Initialized(ApplicationScoped): " + event);
    }
}
