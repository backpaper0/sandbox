package com.example;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = "/fallback", dispatcherTypes = DispatcherType.ERROR)
public class FallbackIndexHtmlFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {

        final String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");

        if (requestUri.startsWith("/api/") == false) {
            request.getRequestDispatcher("index.html").forward(request, response);
            return;
        }

        final int errorStatusCode = (int) request.getAttribute("javax.servlet.error.status_code");
        final String errorMessage = (String) request.getAttribute("javax.servlet.error.message");

        ((HttpServletResponse) response).sendError(errorStatusCode, errorMessage);
    }
}
