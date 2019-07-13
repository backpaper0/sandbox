package com.example;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class SessionTimeoutConfig implements ServletContainerInitializer {

    @Override
    public void onStartup(final Set<Class<?>> c, final ServletContext ctx) throws ServletException {
        //Servlet API 3.1.0にはServletContext#setSessionTimeoutがない
        //ctx.setSessionTimeout(1);
    }
}
