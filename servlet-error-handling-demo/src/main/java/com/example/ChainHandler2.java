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
 * エラーステータスコードのハンドリングをチェーンする例。
 *
 */
@WebServlet(urlPatterns = {
		"/chain2",
		"/chain2b",
		"/chain2c",
})
public class ChainHandler2 extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ChainHandler2.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		switch (req.getRequestURI().substring(req.getContextPath().length())) {
		case "/chain2":
			logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
			Util.ready(req, resp, "ChainHandler2a");
			resp.sendError(504);
			return;
		case "/chain2b":
			if (req.getDispatcherType() == DispatcherType.ERROR) {
				logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
				Util.handle(req, resp, "ChainHandler2b");
				resp.sendError(505);
				return;
			}
			break;
		case "/chain2c":
			if (req.getDispatcherType() == DispatcherType.ERROR) {
				logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
				Util.handle(req, resp, "ChainHandler2c");
				resp.sendError(506);
				return;
			}
			break;
		}
		logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
