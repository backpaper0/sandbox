package sample;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class LogHandler {

    public void handle(@Observes Log log) {
        System.out.printf("Handled: %s%n", log.getText());
    }
}
