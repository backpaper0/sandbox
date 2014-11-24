package app;

import java.util.logging.Logger;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class SessionLog implements HttpSessionAttributeListener {
    private static final Logger logger = Logger.getLogger(SessionLog.class
            .getName());

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        logger.info(() -> "[session attribute added] " + se.getName() + " = "
                + se.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        logger.info(() -> "[session attribute removed] " + se.getName() + " = "
                + se.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        logger.info(() -> "[session attribute replaced] " + se.getName()
                + " = " + se.getValue());
    }
}
