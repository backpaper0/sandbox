package app;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;

public class SessionCookieConfigurator implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        SessionCookieConfig config = ctx.getSessionCookieConfig();
        config.setSecure(true);
        config.setMaxAge((int) TimeUnit.DAYS.toMillis(1));
    }
}
