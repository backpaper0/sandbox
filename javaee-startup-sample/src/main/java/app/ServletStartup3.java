package app;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletStartup3 implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Logs.add("ServletContextListener");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
