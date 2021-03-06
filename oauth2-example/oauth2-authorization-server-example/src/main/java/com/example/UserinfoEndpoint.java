package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/user")
public class UserinfoEndpoint extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        final Principal user = req.getUserPrincipal();

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print("{\"username\":\"" + user.getName() + "\"}");
        }
    }
}
