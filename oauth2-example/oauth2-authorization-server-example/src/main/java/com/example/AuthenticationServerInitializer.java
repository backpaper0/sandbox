package com.example;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AuthenticationServerInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {

        final ServletContext sc = sce.getServletContext();

        final DemoClientRepositoryImpl clientRepository = new DemoClientRepositoryImpl();
        clientRepository
                .add(new Client("exampleclient", "examplesecret", "http://localhost:8080/"));
        ClientRepository.set(sc, clientRepository);

        final DemoUserRepositoryImpl userRepository = new DemoUserRepositoryImpl();
        userRepository.add(new User("demo", "secret"));
        UserRepository.set(sc, userRepository);
    }
}
