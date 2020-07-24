package com.example;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(urlPatterns = "/*")
public class HttpSessionCreator implements Filter {

	private static final Logger logger = Logger
			.getLogger(HttpSessionCreator.class.getName());

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain chain)
			throws IOException, ServletException {
		final var req = ((HttpServletRequest) request);
		if (req.getSession(false) == null) {
			req.getSession();
			logger.info("Session created");
		}
		chain.doFilter(request, response);
	}
}
