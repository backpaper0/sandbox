package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/foo")
public class Foo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        out.println("[Bar]");
        out.println(Bar.class.getClassLoader().getClass());
        out.println(Bar.class.getClassLoader());
        out.println(Bar.class.getProtectionDomain().getCodeSource().getLocation());
        out.println();

        out.println("[Filter]");
        out.println(Filter.class.getClassLoader().getClass());
        out.println(Filter.class.getClassLoader());
        out.println(Filter.class.getProtectionDomain().getCodeSource().getLocation());
        out.println();

        out.flush();
        out.close();
    }
}
