package com.example;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.util.Util;

/**
 * ハンドリングされないステータスコードでsendErrorする。
 *
 */
@WebServlet(urlPatterns = {
		"/status3"
})
public class StatusHandler3 extends HttpServlet {

	private static final Logger logger = Logger.getLogger(StatusHandler3.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
		Util.ready(req, resp, "StatusHandler3");
		resp.sendError(503);
	}
}
