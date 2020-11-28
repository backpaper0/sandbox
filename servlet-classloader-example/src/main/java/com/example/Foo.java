package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/foo")
public class Foo extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();

		print(out, "[Bar]", Bar.class);
		print(out, "[Filter]", Filter.class);

		String orgApacheCatalinaManager = "org.apache.catalina.Manager";
		Class<?> cl;
		try {
			cl = Class.forName(orgApacheCatalinaManager);
		} catch (ClassNotFoundException e) {
			cl = null;
		}
		print(out, "[" + orgApacheCatalinaManager + "]", cl);

		out.flush();
		out.close();
	}

	private static void print(PrintWriter out, String name, Class<?> clazz) {
		out.println(name);
		if (clazz != null) {
			out.println(clazz.getClassLoader().getClass());
			out.println(clazz.getClassLoader());
			out.println(clazz.getProtectionDomain().getCodeSource().getLocation());
		}
		out.println();
	}
}
