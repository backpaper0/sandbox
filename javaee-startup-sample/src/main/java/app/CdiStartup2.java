package app;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

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
