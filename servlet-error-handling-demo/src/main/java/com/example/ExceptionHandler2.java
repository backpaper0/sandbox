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

import com.example.exception.MyException2;
import com.example.util.Util;

/**
 * web.xmlのerror-page要素でexception-type要素を設定する場合の例外ハンドリング。
 * ずっと例外をスローし続けるとどうなるのかを試す。
 *
 */
@WebServlet(urlPatterns = {
		"/exception2"
})
public class ExceptionHandler2 extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ExceptionHandler2.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
		if (req.getDispatcherType() == DispatcherType.ERROR) {
			Util.handle(req, resp, "ExceptionHandler2");
		} else {
			Util.ready(req, resp, "ExceptionHandler2");
		}
		throw new MyException2();
	}
}
