package com.example;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.util.Util;

/**
 * web.xmlのerror-page要素でerror-code要素を設定する場合のエラーハンドリング。
 * sendErrorし続けるとどうなるのかを試す。
 *
 */
@WebServlet(urlPatterns = {
		"/status2"
})
public class StatusHandler2 extends HttpServlet {

	private static final Logger logger = Logger.getLogger(StatusHandler2.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
		if (req.getDispatcherType() == DispatcherType.ERROR) {
			Util.handle(req, resp, "StatusHandler2");
		} else {
			Util.ready(req, resp, "StatusHandler2");
		}
		resp.sendError(502);
	}
}
