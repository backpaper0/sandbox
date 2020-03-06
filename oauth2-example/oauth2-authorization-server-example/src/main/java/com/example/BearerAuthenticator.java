package com.example;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/user")
public class BearerAuthenticator implements Filter {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {

        final String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            response.sendError(401);
            return;
        }
        if (authorization.toLowerCase().startsWith("bearer ") == false) {
            response.sendError(403);
            return;
        }

        final String accessToken = authorization.substring("bearer ".length());

        final User user = AccessToken.getUser(accessToken);
        if (user == null) {
            response.sendError(401);
            return;
        }

        chain.doFilter(new UserHttpServletRequestWrapper(request, user), response);
    }
}
