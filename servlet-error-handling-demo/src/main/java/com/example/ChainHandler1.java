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

import com.example.exception.ChainException1a;
import com.example.exception.ChainException1b;
import com.example.exception.ChainException1c;
import com.example.util.Util;

/**
 * 例外ハンドリングをチェーンする例。
 *
 */
@WebServlet(urlPatterns = {
		"/chain1",
		"/chain1b",
		"/chain1c",
})
public class ChainHandler1 extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ChainHandler1.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		switch (req.getRequestURI().substring(req.getContextPath().length())) {
		case "/chain1":
			logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
			Util.ready(req, resp, "ChainHandler1a");
			throw new ChainException1a();
		case "/chain1b":
			if (req.getDispatcherType() == DispatcherType.ERROR) {
				logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
				Util.handle(req, resp, "ChainHandler1b");
				throw new ChainException1b();
			}
			break;
		case "/chain1c":
			if (req.getDispatcherType() == DispatcherType.ERROR) {
				logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
				Util.handle(req, resp, "ChainHandler1c");
				throw new ChainException1c();
			}
			break;
		}
		logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
