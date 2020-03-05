package com.example;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class FilterInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        final ServletContext context = sce.getServletContext();

        final FilterRegistration.Dynamic reg = context.addFilter(
                OAuth2Filter.class.getSimpleName(), OAuth2Filter.class);
        reg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        reg.setInitParameter("clientId", "exampleclient");
        reg.setInitParameter("clientSecret", "examplesecret");
        reg.setInitParameter("authorizationEndpoint", "http://localhost:9999/authorize");
        reg.setInitParameter("accessTokenEndpoint", "http://localhost:9999/access_token");
        reg.setInitParameter("userinfoEndpoint", "http://localhost:9999/user");
        reg.setInitParameter("redirectUri", "http://localhost:8080/callback");
    }
}
