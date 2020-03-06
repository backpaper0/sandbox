package com.example;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/authorize")
public class AuthorizationEndpoint extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
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

        final Client client = Client.get(clientId);
        if (client == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Client not found: " + clientId);
            return;
        }

        if (client.testRedirectUri(redirectUri) == false) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid redirect URI: " + redirectUri);
            return;
        }

        final User user = (User) req.getSession().getAttribute(User.class.getName());
        final String username = user.getName();

        final String accessToken = UUID.randomUUID().toString();
        AccessToken.associateAccessTokenAndUsername(accessToken, username);

        final String code = UUID.randomUUID().toString();
        AccessToken.associateCodeAndAccessToken(code, accessToken);

        resp.sendRedirect(
                resp.encodeRedirectURL(redirectUri + "?code=" + code + "&state=" + state));
    }
}
