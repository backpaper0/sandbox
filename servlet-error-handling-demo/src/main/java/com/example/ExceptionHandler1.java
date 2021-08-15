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

import com.example.exception.MyException1;
import com.example.util.Util;

/**
 * web.xmlのerror-page要素でexception-type要素を設定する場合の例外ハンドリング。
 *
 */
@WebServlet(urlPatterns = {
		"/exception1"
})
public class ExceptionHandler1 extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ExceptionHandler1.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		if (req.getDispatcherType() != DispatcherType.ERROR) {
			logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
			Util.ready(req, resp, "ExceptionHandler1a");
			throw new MyException1();
		}

		logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
		resp.setStatus(HttpServletResponse.SC_OK);
		Util.handle(req, resp, "ExceptionHandler1b");
	}
}
