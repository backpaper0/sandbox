package com.example;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AuthenticationServerInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        Client.set(new Client("exampleclient", "examplesecret", "http://localhost:8080/"));
        User.set(new User("demo", "secret"));
    }
}
