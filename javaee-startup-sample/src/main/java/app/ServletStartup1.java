package app;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ServletStartup1 implements
        ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx)
            throws ServletException {
        Logs.add("ServletContainerInitializer");
    }
}
