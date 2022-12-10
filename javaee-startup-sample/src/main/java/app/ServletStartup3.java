package app;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

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
