package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = "/authorize")
public class AuthorizationEndpoint extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        final String authorization = req.getParameter("authorization");
        if (authorization != null) {
            final String csrfToken = req.getParameter("csrf_token");
            final HttpSession session = req.getSession();
            if (CsrfToken.test(session, csrfToken) == false) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid CSRF token: " + csrfToken);
                return;
            }
            final String clientId = req.getParameter("client_id");
            if (authorization.equals("allow")) {
                final User user = User.get(req.getSession());
                final ClientRepository clientRepository = ClientRepository
                        .get(req.getServletContext());
                final Client client = clientRepository.find(clientId);
                final AuthorizationRepository authorizationRepository = AuthorizationRepository
                        .get(req.getServletContext());
                authorizationRepository.allow(user, client);
            }
        }
        process(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    private void process(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        final String responseType = req.getParameter("response_type");
        final String clientId = req.getParameter("client_id");
        final String redirectUri = req.getParameter("redirect_uri");
        final String scope = req.getParameter("scope");
        final String state = req.getParameter("state");

        if (Objects.equals(responseType, "code") == false) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid response type: " + responseType);
            return;
        }

        final ClientRepository clientRepository = ClientRepository.get(req.getServletContext());
        final Client client = clientRepository.find(clientId);
        if (client == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Client not found: " + clientId);
            return;
        }

        if (client.testRedirectUri(redirectUri) == false) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid redirect URI: " + redirectUri);
            return;
        }

        final User user = User.get(req.getSession());
        final String username = user.getName();

        final AuthorizationRepository authorizationRepository = AuthorizationRepository
                .get(req.getServletContext());

        if (authorizationRepository.test(user, client)) {
            final String accessToken = UUID.randomUUID().toString();
            AccessToken.associateAccessTokenAndUsername(accessToken, username);

            final String code = UUID.randomUUID().toString();
            AccessToken.associateCodeAndAccessToken(code, accessToken);

            resp.sendRedirect(
                    resp.encodeRedirectURL(redirectUri + "?code=" + code + "&state=" + state));
            return;
        }

        final HttpSession session = req.getSession();
        resp.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print("<!doctype html>");
            out.print("<html>");
            out.print("<head>");
            out.print("<meta charset=\"utf-8\">");
            out.print(
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
            out.print("<title>Authorization</title>");
            out.print("</head>");
            out.print("<body>");
            out.print("<h1>OAuth 2.0 - Authorization</h1>");
            out.printf("<p>clientId - %s</p>", clientId);
            out.printf("<p>scope - %s</p>", scope);
            out.print("<form method=\"POST\">");
            out.print(
                    "<p><label><input type=\"radio\" name=\"authorization\" value=\"allow\"> Allow</label></p>");
            out.print(
                    "<p><label><input type=\"radio\" name=\"authorization\" value=\"deny\"> Deny</label></p>");
            out.print("<p>");
            out.print("<button type=\"submit\">Submit</submit>");
            out.printf("<input type=\"hidden\" name=\"csrf_token\" value=\"%s\">",
                    CsrfToken.get(session));
            out.print("</p>");
            out.print("</form>");
            out.print("</body>");
            out.print("</html>");
        }
    }
}
