package com.example;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.SessionCookieConfig;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        final SessionCookieConfig config = sce.getServletContext().getSessionCookieConfig();
        config.setName("customSessionId");
        config.setHttpOnly(true);
        config.setMaxAge((int) TimeUnit.HOURS.toSeconds(1));
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
    }
}
