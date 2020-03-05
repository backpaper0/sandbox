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

        final FilterRegistration.Dynamic protection = context.addFilter("ProtectionFilter",
                OAuth2Filter.class);
        protection.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
    }
}
