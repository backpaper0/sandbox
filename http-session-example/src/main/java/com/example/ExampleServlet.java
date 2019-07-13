package com.example;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/*")
public class ExampleServlet extends HttpServlet {

    private String getName(final HttpServletRequest req) {
        final String path = req.getRequestURI();
        return path.substring(1);
    }

    private String getValue(final HttpServletRequest req) throws IOException {
        try (BufferedReader in = req.getReader()) {
            return in.readLine();
        }
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final String name = getName(req);
        final Object value = req.getSession().getAttribute(name);
        resp.setStatus(200);
        resp.getWriter().println(value);
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final String name = getName(req);
        final String value = getValue(req);
        req.getSession().setAttribute(name, value);
        resp.setStatus(204);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final String name = getName(req);
        req.getSession().removeAttribute(name);
        resp.setStatus(204);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        switch (req.getRequestURI()) {
        case "/changeId": {
            req.changeSessionId();
            resp.setStatus(204);
            break;
        }
        case "/invalidate": {
            req.getSession().invalidate();
            resp.setStatus(204);
            break;
        }
        case "/meta": {
            resp.setStatus(204);
            final HttpSession session = req.getSession();
            resp.getWriter().printf("id = %s%nmaxInactiveInterval = %s%n",
                    session.getId(),
                    session.getMaxInactiveInterval());
            break;
        }
        default:
            resp.setStatus(404);
        }
    }
}
