package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/access_token")
public class AccessTokenEndpoint extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        final String grantType = req.getParameter("grant_type");
        final String code = req.getParameter("code");
        final String redirectUri = req.getParameter("redirect_uri");
        final String clientId = req.getParameter("client_id");

        if (Objects.equals(grantType, "authorization_code") == false) {
            resp.sendError(400);
            return;
        }

        final Client client = Client.get(clientId);

        if (client.testRedirectUri(redirectUri) == false) {
            resp.sendError(400);
            return;
        }

        final String accessToken = AccessToken.getAccessToken(code);

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print("{\"access_token\":\"" + accessToken + "\"}");
        }
    }
}
