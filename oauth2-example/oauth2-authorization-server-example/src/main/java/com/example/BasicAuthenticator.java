package com.example;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/access_token")
public class BasicAuthenticator implements Filter {

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
        if (authorization.toLowerCase().startsWith("basic ") == false) {
            response.sendError(403);
            return;
        }

        final String[] strs = new String(
                Base64.getDecoder().decode(authorization.substring("basic ".length()))).split(":");

        final String clientId = strs[0];
        final String clientSecret = strs[1];

        final Client client = Client.get(clientId);
        if (client == null) {
            response.sendError(403);
            return;
        }

        if (client.testClientSecret(clientSecret) == false) {
            response.sendError(401);
            return;
        }

        chain.doFilter(request, response);
    }
}
