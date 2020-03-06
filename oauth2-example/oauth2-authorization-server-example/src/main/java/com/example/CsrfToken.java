package com.example;

import java.util.UUID;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class CsrfToken implements HttpSessionListener, HttpSessionIdListener {

    @Override
    public void sessionCreated(final HttpSessionEvent se) {
        se.getSession().setAttribute(CsrfToken.class.getName(), UUID.randomUUID().toString());
    }

    @Override
    public void sessionIdChanged(final HttpSessionEvent event, final String oldSessionId) {
        event.getSession().setAttribute(CsrfToken.class.getName(), UUID.randomUUID().toString());
    }

    public static String get(final HttpSession session) {
        return (String) session.getAttribute(CsrfToken.class.getName());
    }

    public static boolean test(final HttpSession session, final String csrfToken) {
        return csrfToken != null && csrfToken.equals(get(session));
    }
}
