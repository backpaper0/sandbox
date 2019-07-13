package com.example;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class HttpSessionBindingListenerImpl implements HttpSessionBindingListener {

    @Override
    public void valueBound(final HttpSessionBindingEvent event) {
        System.out.printf("%s[%s] valueBound(name = %s)%n",
                LocalDateTime.now(),
                event.getSession().getId(),
                event.getName());
    }

    @Override
    public void valueUnbound(final HttpSessionBindingEvent event) {
        System.out.printf("%s[%s] valueUnbound(name = %s)%n",
                LocalDateTime.now(),
                event.getSession().getId(),
                event.getName());
    }
}
