package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final Object user = req.getSession().getAttribute("user");
		resp.setContentType("application/json");
		try (PrintWriter out = resp.getWriter()) {
			out.print("{\"message\":\"Hello, " + user + "!\"}");
			out.flush();
		}
	}
}
