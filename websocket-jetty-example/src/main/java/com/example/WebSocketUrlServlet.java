package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/url")
public class WebSocketUrlServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String token = UUID.randomUUID().toString();
		final String user = req.getParameter("user");
		Messages.put(token, user);
		resp.setContentType("application/json");
		try (PrintWriter out = resp.getWriter()) {
			out.println("{\"url\":\"ws://localhost:8080/ws?token=" + token + "\"}");
			out.flush();
		}
	}
}
