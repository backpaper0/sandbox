package com.example;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ServletContainerInitializerImpl implements ServletContainerInitializer {

    @Override
    public void onStartup(final Set<Class<?>> c, final ServletContext ctx) throws ServletException {

        final AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
        wac.register(MvcConfig.class);
        wac.refresh();

        final DispatcherServlet servlet = new DispatcherServlet(wac);
        final ServletRegistration.Dynamic reg = ctx.addServlet("dispatcher", servlet);
        reg.addMapping("/*");
    }
}
