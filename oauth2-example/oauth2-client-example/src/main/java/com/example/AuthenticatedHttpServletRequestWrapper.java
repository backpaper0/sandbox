package com.example;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class AuthenticatedHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public AuthenticatedHttpServletRequestWrapper(final HttpServletRequest request) {
        super(request);
    }

    @Override
    public Principal getUserPrincipal() {
        final HttpSession session = ((HttpServletRequest) getRequest()).getSession(false);
        if (session == null) {
            return null;
        }
        return (Principal) session.getAttribute("userPrincipal");
    }
}
