package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/request_dispatcher")
public class GetRequestDispatcher extends HttpServlet {

	private static final Logger logger = Logger.getLogger(GetRequestDispatcher.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String path = req.getParameter("path");
		final RequestDispatcher rd = req.getRequestDispatcher(path);
		if (logger.isLoggable(Level.INFO)) {
			final String s = path != null ? '"' + path + '"' : path;
			logger.log(Level.INFO, "path = {0}, rd = {1}", new Object[] { s, rd });
		}
		resp.setContentType("text/plain");
		final PrintWriter out = resp.getWriter();
		out.print(rd);
		out.flush();
		out.close();
	}
}
