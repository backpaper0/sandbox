package com.example;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "foo.do")
public class Servlet2Jsp extends HttpServlet {

    private String servletName;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        servletName = config.getServletName();
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final String requestURI = req.getRequestURI();
        System.out.printf(">>> %s (%s)%n", servletName, requestURI);
        resp.setContentType("text/html; charset=UTF-8");
        req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
        System.out.printf("<<< %s (%s)%n", servletName, requestURI);
    }
}
