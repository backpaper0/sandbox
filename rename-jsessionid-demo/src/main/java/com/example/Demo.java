package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/1", "/2" })
public class Demo extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String requestURI = req.getRequestURI();

		if (requestURI.startsWith(req.getContextPath())) {
			requestURI = requestURI.substring(req.getContextPath().length());
		}

		if (requestURI.equals("/1")) {
			resp.setContentType("text/plain");
			PrintWriter out = resp.getWriter();
			HttpSession session = req.getSession();
			out.printf("HttpSession ID: %s%n", session.getId());
			UUID uuid = UUID.randomUUID();
			session.setAttribute("uuid", uuid);
			out.printf("UUID: %s%n", uuid);
			resp.addCookie(new Cookie("jsessionid", uuid.toString()));

		} else if (requestURI.equals("/2")) {
			resp.setContentType("text/plain");
			PrintWriter out = resp.getWriter();
			Cookie[] cookies = req.getCookies();
			for (Cookie cookie : cookies) {
				out.printf("Cookie: %s = %s%n", cookie.getName(), cookie.getValue());
			}
			HttpSession session = req.getSession();
			out.printf("HttpSession ID: %s%n", session.getId());
			Object uuid = session.getAttribute("uuid");
			out.printf("UUID: %s%n", uuid);

		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
