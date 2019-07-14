package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.doma.Entity;

@WebServlet(urlPatterns = "/classloader")
public class ClassLoaderServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(200);
        try (PrintWriter out = resp.getWriter()) {
            final Exmple example = new Exmple(out);
            example.process("ClassLoader.getSystemClassLoader()",
                    ClassLoader.getSystemClassLoader());
            example.process("Object.class.getClassLoader()", Object.class.getClassLoader());
            example.process("getClass().getClassLoader()", getClass().getClassLoader());
            example.process("Entity.class.getClassLoader()", Entity.class.getClassLoader());
            example.process("Thread.currentThread().getContextClassLoader()",
                    Thread.currentThread().getContextClassLoader());
        }
    }

    private static class Exmple {
        PrintWriter out;
        int counter;

        public Exmple(final PrintWriter out) {
            this.out = out;
        }

        void process(final String name, final ClassLoader classLoader) {
            out.printf("[%s]%s%n", ++counter, name);
            if (classLoader != null) {
                out.printf("  ClassLoader = %s%n", classLoader);
                if (classLoader instanceof URLClassLoader) {
                    final URL[] urLs = ((URLClassLoader) classLoader).getURLs();
                    for (final URL url : urLs) {
                        out.printf("    %s%n", url);
                    }
                }
            }
        }
    }
}
