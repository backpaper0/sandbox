package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/foo")
public class FooServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final List<Class<?>> classes = new ArrayList<Class<?>>(
				(Set<Class<?>>) req.getServletContext()
						.getAttribute(Foo.class.getName()));
		Collections.sort(classes, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		resp.setContentType("text/plain");
		final PrintWriter out = resp.getWriter();
		for (final Class<?> clazz : classes) {
			out.println(clazz.getName());
		}
		out.flush();
		out.close();
	}
}
