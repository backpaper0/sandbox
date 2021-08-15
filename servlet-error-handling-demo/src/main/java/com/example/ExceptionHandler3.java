package com.example;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.exception.MyException3;
import com.example.util.Util;

/**
 * ハンドリングされない例外をスローする。
 *
 */
@WebServlet(urlPatterns = {
		"/exception3"
})
public class ExceptionHandler3 extends HttpServlet {

	private static final Logger logger = Logger.getLogger(ExceptionHandler3.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.log(Level.INFO, "{0} {1}", new Object[] { req.getMethod(), req.getRequestURI() });
		Util.ready(req, resp, "ExceptionHandler3");
		throw new MyException3();
	}
}
