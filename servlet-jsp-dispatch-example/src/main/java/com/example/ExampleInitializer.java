package com.example;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ExampleInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        final ServletContext sc = sce.getServletContext();

        for (final DispatcherType dispatcherType : DispatcherType.values()) {
            final String filterName = String.format("%s[%s]", ExampleFilter.class.getSimpleName(),
                    dispatcherType.name());
            final EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(dispatcherType);
            sc.addFilter(filterName, ExampleFilter.class)
                    .addMappingForUrlPatterns(dispatcherTypes, false, "*.do", "*.jsp");
        }

        final String filterName = String.format("%s[%s]", ExampleFilter.class.getSimpleName(),
                "*");
        final EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        sc.addFilter(filterName, ExampleFilter.class)
                .addMappingForUrlPatterns(dispatcherTypes, false, "*.do", "*.jsp");
    }
}
