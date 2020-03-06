package com.example;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class UserHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final User user;

    public UserHttpServletRequestWrapper(final HttpServletRequest request, final User user) {
        super(request);
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }
}
