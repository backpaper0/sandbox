package com.example;

import java.time.LocalDateTime;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class HttpSessionListenerImpl
        implements HttpSessionListener, HttpSessionAttributeListener, HttpSessionIdListener {

    @Override
    public void sessionIdChanged(final HttpSessionEvent event, final String oldSessionId) {
        System.out.printf("%s[%s] sessionIdChanged(%s -> %s)%n",
                LocalDateTime.now(),
                event.getSession().getId(),
                oldSessionId,
                event.getSession().getId());
    }

    @Override
    public void attributeAdded(final HttpSessionBindingEvent event) {
        System.out.printf("%s[%s] attributeAdded(name = %s, value = %s)%n",
                LocalDateTime.now(),
                event.getSession().getId(),
                event.getName(),
                event.getValue());
    }

    @Override
    public void attributeRemoved(final HttpSessionBindingEvent event) {
        System.out.printf("%s[%s] attributeRemoved(name = %s, value = %s)%n",
                LocalDateTime.now(),
                event.getSession().getId(),
                event.getName(),
                event.getValue());
    }

    @Override
    public void attributeReplaced(final HttpSessionBindingEvent event) {
        System.out.printf("%s[%s] attributeReplaced(name = %s, value = %s)%n",
                LocalDateTime.now(),
                event.getSession().getId(),
                event.getName(),
                event.getValue());
    }

    @Override
    public void sessionCreated(final HttpSessionEvent se) {
        System.out.printf("%s[%s] sessionCreated%n",
                LocalDateTime.now(),
                se.getSession().getId());
        se.getSession().setAttribute("listener", new HttpSessionBindingListenerImpl());
    }

    @Override
    public void sessionDestroyed(final HttpSessionEvent se) {
        System.out.printf("%s[%s] sessionDestroyed%n",
                LocalDateTime.now(),
                se.getSession().getId());
    }
}
