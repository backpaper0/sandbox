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
public class ClientBasicAuthenticator implements Filter {

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
            response.setHeader("WWW-Authenticate", "Basic realm=Authorization Server");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (authorization.toLowerCase().startsWith("basic ") == false) {
            response.setHeader("WWW-Authenticate", "Basic realm=Authorization Server");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final String[] strs = new String(
                Base64.getDecoder().decode(authorization.substring("basic ".length()))).split(":");

        final String clientId = strs[0];
        final String clientSecret = strs[1];

        final ClientRepository clientRepository = ClientRepository.get(request.getServletContext());
        final Client client = clientRepository.find(clientId);
        if (client == null) {
            response.setHeader("WWW-Authenticate", "Basic realm=Authorization Server");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (client.testClientSecret(clientSecret) == false) {
            response.setHeader("WWW-Authenticate", "Basic realm=Authorization Server");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}
