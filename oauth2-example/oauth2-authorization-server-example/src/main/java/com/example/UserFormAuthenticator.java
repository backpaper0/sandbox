package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/authorize")
public class UserFormAuthenticator implements Filter {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {
        doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain)
            throws IOException, ServletException {

        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(User.class.getName());
        if (user != null) {
            chain.doFilter(request, response);
            return;
        }

        if (request.getMethod().equals("GET")) {
            response.setContentType("text/html; charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.print("<!doctype html>");
                out.print("<html>");
                out.print("<head>");
                out.print("<meta charset=\"utf-8\">");
                out.print(
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
                out.print("<title>Authentication</title>");
                out.print("</head>");
                out.print("<body>");
                out.printf("<p>scope - %s</p>", request.getParameter("scope"));
                out.print("<form method=\"POST\">");
                out.print("<p><input type=\"text\" name=\"username\"></p>");
                out.print("<p><input type=\"password\" name=\"password\"></p>");
                out.print("<p>");
                out.print("<button type=\"submit\">Login</submit>");
                out.printf("<input type=\"hidden\" name=\"csrf_token\" value=\"%s\">",
                        CsrfToken.get(session));
                out.print("</p>");
                out.print("</form>");
                out.print("</body>");
                out.print("</html>");
            }
        } else if (request.getMethod().equals("POST")) {
            final String username = request.getParameter("username");
            final String password = request.getParameter("password");
            final String csrfToken = request.getParameter("csrf_token");

            if (CsrfToken.test(session, csrfToken) == false) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid CSRF token: " + csrfToken);
                return;
            }
            final User user2 = User.get(username);
            if (user2 == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "User is not found: " + username);
                return;
            }
            if (user2.testPassword(password) == false) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Unmatch password: " + password);
                return;
            }
            session.setAttribute(User.class.getName(), user2);
            chain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }
}
