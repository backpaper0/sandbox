package com.example.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {
		"/*"
}, dispatcherTypes = {
		DispatcherType.FORWARD,
		DispatcherType.INCLUDE,
		DispatcherType.REQUEST,
		DispatcherType.ASYNC,
		DispatcherType.ERROR
})
public class RequestLoggingFilter implements Filter {

	private static final Logger logger = Logger.getLogger(RequestLoggingFilter.class.getName());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logging((HttpServletRequest) request, (HttpServletResponse) response);
		chain.doFilter(request, response);
	}

	private void logging(HttpServletRequest request, HttpServletResponse response) {
		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);
		out.printf("%s %s%n", request.getMethod(), request.getRequestURI());
		out.printf("DispatcherType: %s%n", request.getDispatcherType());
		Enumeration<String> names = request.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			Object attribute = request.getAttribute(name);
			out.printf("%s: %s%n", name, attribute);
		}
		out.flush();
		out.close();
		logger.info(sw.toString());
	}
}
