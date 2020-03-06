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
public class UserBearerAuthenticator implements Filter {

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
            response.setHeader("WWW-Authenticate", "Bearer realm=Authorization Server");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (authorization.toLowerCase().startsWith("bearer ") == false) {
            response.setHeader("WWW-Authenticate", "Bearer realm=Authorization Server");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final String accessToken = authorization.substring("bearer ".length());

        final String username = AccessToken.getUser(accessToken);
        if (username == null) {
            response.setHeader("WWW-Authenticate", "Bearer realm=Authorization Server");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        final UserRepository userRepository = UserRepository.get(request.getServletContext());
        final User user = userRepository.find(username);
        if (user == null) {
            response.setHeader("WWW-Authenticate", "Bearer realm=Authorization Server");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(new UserHttpServletRequestWrapper(request, user), response);
    }
}
