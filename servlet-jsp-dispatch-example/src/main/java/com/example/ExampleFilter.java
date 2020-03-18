package com.example;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ExampleFilter implements Filter {

    private String filterName;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        filterName = filterConfig.getFilterName();
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {
        final String requestURI = ((HttpServletRequest) request).getRequestURI();
        System.out.printf(">>> %s (%s)%n", filterName, requestURI);
        chain.doFilter(request, response);
        System.out.printf("<<< %s (%s)%n", filterName, requestURI);
    }
}
